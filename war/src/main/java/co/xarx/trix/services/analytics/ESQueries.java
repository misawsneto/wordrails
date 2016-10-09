package co.xarx.trix.services.analytics;

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

	private List<String> getIndexFields(String fieldName, Map<String, Object> mapProperties) {
		List<String> fieldList = new ArrayList<>();
		Map<String, Object> map = (Map<String, Object>) mapProperties.get("properties");
		Set<String> keys = map.keySet();
		for (String key : keys) {
			if (((Map<String, Object>) map.get(key)).containsKey("type")) {
				fieldList.add(fieldName + "" + key);
			} else {
				List<String> tempList = getIndexFields(fieldName + "" + key + ".", (Map<String, Object>) map.get(key));
				fieldList.addAll(tempList);
			}
		}
		return fieldList;
	}

	public boolean isValidField(String field) throws Exception {
		if (getNginxFields().contains(field)) {
			return true;
		} else return false;
	}


	public List<String> getNginxFields() throws Exception {
		ClusterState cs = client.admin().cluster().prepareState().setIndices(accessIndex).execute().actionGet().getState();

		IndexMetaData indexMetaData = cs.getMetaData().index(accessIndex);
		if (indexMetaData == null) {
			throw new Exception("The data cannot be retrived: No index metadata");
		}

		MappingMetaData mappingMetaData = indexMetaData.mapping(Constants.ObjectType.ANALYTICS_INDEX_TYPE);
		Map<String, Object> map = null;

		try {
			map = mappingMetaData.getSourceAsMap();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return getIndexFields("", map);
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

	private SearchRequestBuilder prepareRangkingQuery(String field, Interval interval, Integer size, String term, BoolQueryBuilder must){
		SearchRequestBuilder search = client.prepareSearch();
		search.setTypes(Constants.ObjectType.ANALYTICS_INDEX_TYPE).addAggregation(AggregationBuilders.terms(term).field(field).size(size));
		search.addAggregation(AggregationBuilders.range("timestamp")
				.field("timestamp").addRange(interval.getStart().getMillis(), interval.getEnd().getMillis()));

		if(must != null) search.setQuery(must);

		return search;
	}

	public Map findMostPopular(String field, String byField, Object byValue, Interval interval, Integer size) throws Exception {
		if (!isValidField(field)) {
			throw new Exception("Invalid field in nginx: " + field);
		}
		String term = "popular_" + field;
		BoolQueryBuilder must = null;

		if(byField != null && !byField.isEmpty() && byValue != null){
			if(!isValidField(byField)) throw new Exception("Invalid field in nginx: " + byField);

			must = new BoolQueryBuilder();
			must.must(termQuery(byField, byValue));
			term += "_by_" + byField;
		}

		SearchRequestBuilder search = prepareRangkingQuery(field, interval, size, term, must);
		SearchResponse response = search.execute().actionGet();

		Terms terms = response.getAggregations().get(term);
		return parseRanking(terms);
	}

	private Map parseRanking(Terms results){
		Map buckets = new HashMap();
		for (Terms.Bucket b : results.getBuckets()) {
			buckets.put(b.getKey(), b.getDocCount());
		}

		return buckets;
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
