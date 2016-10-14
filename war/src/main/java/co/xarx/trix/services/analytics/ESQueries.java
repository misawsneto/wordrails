package co.xarx.trix.services.analytics;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.util.Constants;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.TermQuery;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class ESQueries {

	private Client client;
	private String accessIndex;
	private Map<Class<?>, String> requestSearchFields;

	@Autowired
	public ESQueries(Client client,
					  @Value("${elasticsearch.access_index}") String accessIndex){

		this.client = client;
		this.accessIndex = accessIndex;
		this.requestSearchFields = loadRequestSearchFields();
	}

	private Map loadRequestSearchFields() {
		Map fields = new HashMap<Class<?>, String>();

		fields.put(Post.class, "postId");
		fields.put(Person.class, "authorId");
		fields.put(Station.class, "stationId");
		fields.put(Network.class, "tenantId");

		return fields;
	}

	private Map<Long, Long> generalCounter(String queryName, QueryBuilder query, String orderField) {
		SearchRequestBuilder search = client.prepareSearch(accessIndex);

		search.setQuery(query);
		search.addAggregation(AggregationBuilders.dateHistogram(queryName).field(orderField).interval(DateHistogram.Interval.DAY));

		SearchResponse response = search.execute().actionGet();
		DateHistogram histogram = response.getAggregations().get(queryName);

		HashMap hist = new HashMap();

		for (DateHistogram.Bucket b : histogram.getBuckets()) {
			hist.put(b.getKeyAsDate().toDate().getTime(), b.getDocCount());
		}

		return hist;
	}

	public Map<Long, Long> getReadsByEntity(AnalyticsEntity entity){
		String fieldName = requestSearchFields.get(entity.getClass());
		String queryName = "reads_by_" + fieldName;
		Object id = getEntityIdentifier(entity);

		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(termQuery(fieldName, id));
		query.must(termQuery("action", Constants.StatsEventType.POST_READ));

		return generalCounter(queryName, query, "timestamp");
	}

	public Map<Long, Long> getRecommendsByEntity(AnalyticsEntity entity){
		String fieldName = requestSearchFields.get(entity.getClass());
		String queryName = "comments_by_" + fieldName;
		Object id = getEntityIdentifier(entity);

		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(termQuery(fieldName, id));
		query.must(termQuery("action", Constants.StatsEventType.POST_RECOMMEND));

		return generalCounter(queryName, query, "timestamp");
	}

	public Map<Long, Long> getCommentsByEntity(AnalyticsEntity entity){
		String fieldName = requestSearchFields.get(entity.getClass());
		String queryName = "comments_by_" + fieldName;
		Object id = getEntityIdentifier(entity);

		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(termQuery(fieldName, id));
		query.must(termQuery("action", Constants.StatsEventType.POST_COMMENT));

		return generalCounter(queryName, query, "timestamp");
	}

	private SearchRequestBuilder prepareRangkingQuery(String field, Interval interval, String term, BoolQueryBuilder must, Integer size, Integer page){
		SearchRequestBuilder search = client.prepareSearch();
		search.addAggregation(AggregationBuilders
				.terms(term)
				.field(field)
				// page should be bigger than zero
				.size(size * (page + 1)));
		search.addAggregation(AggregationBuilders.range("timestamp")
				.field("timestamp").addRange(interval.getStart().getMillis(), interval.getEnd().getMillis()));

		if(must != null) search.setQuery(must);

		return search;
	}

	public Map findMostPopular(String field, String byField, Object byValue, Interval interval, Integer size, Integer page) throws Exception {
		String term = "popular_" + field;
		BoolQueryBuilder must = new BoolQueryBuilder();
        must.must(termQuery("tenantId", TenantContextHolder.getCurrentTenantId()));

		if(byField != null && !byField.isEmpty() && byValue != null){
			must.must(termQuery(byField, byValue));
			term += "_by_" + byField;
		}

		SearchRequestBuilder search = prepareRangkingQuery(field, interval, term, must, size, page);
		SearchResponse response = search.execute().actionGet();

		Terms terms = response.getAggregations().get(term);
		return parseRanking((List) terms.getBuckets(), size, page);
	}

	private Map parseRanking(List<Terms.Bucket> results, Integer size, Integer page){
		Map buckets = new HashMap();

		int start = (size + 1) * page;
		int end = results.size();

		for (Terms.Bucket b : results.subList(start, end)) {
			buckets.put(b.getKey(), b.getDocCount());
		}
		return sortByValue(buckets);
	}

	private Map<String, Long> sortByValue(Map<String, Long> unsortMap) {

		LinkedList<Map.Entry<String, Long>> list =
				new LinkedList<Map.Entry<String, Long>>(unsortMap.entrySet());

		Collections.sort(list, new Comparator<Map.Entry<String, Long>>() {
			public int compare(Map.Entry<String, Long> o1,
							   Map.Entry<String, Long> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		Map<String, Long> sortedMap = new LinkedHashMap<String, Long>();
		for (Map.Entry<String, Long> entry : list) {
			sortedMap.put(entry.getKey(), entry.getValue());
		}

		return sortedMap;
	}


	public Integer countActionsByEntity(String action, AnalyticsEntity entity){
		String fieldName = requestSearchFields.get(entity.getClass());
		Object id = getEntityIdentifier(entity);

		return countTotals(id, fieldName, action);
	}

	public Integer countTotals(Object id, String field, String action) {
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		boolQuery.must(termQuery(field, id));
		boolQuery.must(termQuery("action", action));

		SearchRequestBuilder search = client.prepareSearch(accessIndex);
		search.setQuery(boolQuery);

		return (int) search.execute().actionGet().getHits().getTotalHits();
	}

	private Object getEntityIdentifier(AnalyticsEntity entity) {
		String field = requestSearchFields.get(entity.getClass());
		Object id;
		if(field.equals(Constants.ObjectType.ANALYTICS_INDEX_TYPE + ".tenantId")){
			id = entity.getTenantId();
		} else {
			id = entity.getId();
		}
		return id;
	}

}
