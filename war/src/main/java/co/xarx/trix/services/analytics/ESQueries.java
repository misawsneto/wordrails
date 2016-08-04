package co.xarx.trix.services.analytics;

import co.xarx.trix.domain.*;
import co.xarx.trix.util.Constants;
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
					  @Value("${elasticsearch.nginxAccessIndex}") String accessIndex){

		this.client = client;
		this.accessIndex = accessIndex;
		this.requestSearchFields = loadRequestSearchFields();
	}

	@PostConstruct
	public void checkIndices() throws ExecutionException, InterruptedException {
		boolean isThereNginxIndex = client.admin().indices().exists(new IndicesExistsRequest(accessIndex)).get().isExists();
		Assert.isTrue(isThereNginxIndex, "Big problem! Index is not there: " + accessIndex);
	}

	private Map loadRequestSearchFields() {
		Map fields = new HashMap<Class<?>, String>();

		fields.put(Post.class, "nginx_access.postId");
		fields.put(Person.class, "nginx_access.authorId");
		fields.put(Station.class, "nginx_access.stationId");
		fields.put(Network.class, "nginx_access.tenantId");

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

		MappingMetaData mappingMetaData = indexMetaData.mapping(Constants.AnalyticsType.READ);
		Map<String, Object> map = null;

		try {
			map = mappingMetaData.getSourceAsMap();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return getIndexFields("", map);
	}

	private Map generalCounter(String queryName, QueryBuilder query, String orderField) {
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

	public Map getReadsByEntity(AnalyticsEntity entity){
		String fieldName = requestSearchFields.get(entity.getClass());
		String queryName = "reads_by_" + fieldName;
		Object id = getEntityIdentifier(entity);

		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(termQuery(fieldName, id));
		query.must(termQuery("action", "postread"));
		query.must(termQuery("verb", "get"));
		query.must(termQuery("_type", Constants.AnalyticsType.READ));

		return generalCounter(queryName, query, "@timestamp");
	}

	public Map getCommentsByEntity(AnalyticsEntity entity){
		String fieldName = requestSearchFields.get(entity.getClass());
		String queryName = "comments_by_" + fieldName;
		Object id = getEntityIdentifier(entity);

		BoolQueryBuilder query = new BoolQueryBuilder();
		query.must(termQuery(fieldName, id));
		query.must(termQuery("action", "comment"));
		query.must(termQuery("verb", "post"));
		query.must(termQuery("_type", Constants.AnalyticsType.READ));

		return generalCounter(queryName, query, "@timestamp");
	}

	public Map findMostPopular(String field, Interval interval, Integer size) throws Exception {
		if (!isValidField(field)) {
			throw new Exception("Invalid field in nginx: " + field);
		}

		String term = "by_" + field;

		SearchRequestBuilder search = client.prepareSearch();
		search.setTypes(Constants.AnalyticsType.READ).addAggregation(AggregationBuilders.terms(term).field(field).size(size));

		search.addAggregation(AggregationBuilders.range("timestamp")
				.field("@timestamp").addRange(interval.getStart().getMillis(), interval.getEnd().getMillis()));

		SearchResponse response = search.execute().actionGet();

		Terms networks = response.getAggregations().get(term);
		Map buckets = new HashMap();

		for (Terms.Bucket b : networks.getBuckets()) {
			buckets.put(b.getKey(), b.getDocCount());
		}

		return buckets;
	}

	public Integer countActionsByEntity(String action, String httpVerb, AnalyticsEntity entity){
		String fieldName = requestSearchFields.get(entity.getClass());
		Object id = getEntityIdentifier(entity);

		return countTotals(id, fieldName, action, httpVerb);
	}

	public Integer countTotals(Object id, String field, String action, String httpVerb) {
		BoolQueryBuilder boolQuery = new BoolQueryBuilder();
		boolQuery.must(termQuery(field, id));
		boolQuery.must(termQuery("verb", httpVerb));
		boolQuery.must(termQuery("action", action));

		SearchRequestBuilder search = client.prepareSearch(accessIndex);
		search.setQuery(boolQuery);

		return (int) search.execute().actionGet().getHits().getTotalHits();
	}

	private Object getEntityIdentifier(AnalyticsEntity entity) {
		String field = requestSearchFields.get(entity.getClass());
		Object id;
		if(field.equals("nginx_access.tenantId")){
			id = entity.getTenantId();
		} else {
			id = entity.getId();
		}
		return id;
	}

}
