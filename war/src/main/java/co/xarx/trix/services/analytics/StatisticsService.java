package co.xarx.trix.services.analytics;

import co.xarx.trix.domain.Person;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.ReadsCommentsRecommendsCount;
import co.xarx.trix.util.StatsJson;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class StatisticsService {

	public ObjectMapper mapper;
	private Client client;
	private String analyticsIndex;
	private AuthService authService;

	@Autowired
	public StatisticsService(@Qualifier("objectMapper") ObjectMapper mapper,
							 Client client,
							 @Value("${elasticsearch.analyticsIndex}") String analyticsIndex,
							 AuthService authService){
		this.analyticsIndex = analyticsIndex;
		this.authService = authService;
		this.mapper = mapper;
		this.client = client;
	}

	public HashMap getPorpularNetworks(){
		return findMostPopular("network", null, null, 10);
	}

	public List<String> getNginxFields(){
		ClusterState cs = client.admin().cluster().prepareState().setIndices(analyticsIndex).execute().actionGet().getState();

		IndexMetaData indexMetaData = cs.getMetaData().index(analyticsIndex);
		MappingMetaData mappingMetaData = indexMetaData.mapping(Constants.ObjectType.NGINX_INDEX);
		Map<String, Object> map = null;

		try {
			map = mappingMetaData.getSourceAsMap();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return getList("", map);
	}

	private static List<String> getList(String fieldName, Map<String, Object> mapProperties) {
		List<String> fieldList = new ArrayList<String>();
		Map<String, Object> map = (Map<String, Object>) mapProperties.get("properties");
		Set<String> keys = map.keySet();
		for (String key : keys) {
			if (((Map<String, Object>) map.get(key)).containsKey("type")) {
				fieldList.add(fieldName + "" + key);
			} else {
				List<String> tempList = getList(fieldName + "" + key + ".", (Map<String, Object>) map.get(key));
				fieldList.addAll(tempList);
			}
		}
		return fieldList;
	}

	private boolean isValidField(String field){
		if(getNginxFields().contains(field)) {
			return true;
		} else return false;
	}

	public boolean isValidTimeRange(Long begin, Long end){
		return begin != null && end != null && begin < end;
	}

	public HashMap findMostPopular(String field, Long startTimestamp, Long endTimestamp, Integer size){
		Assert.hasText(field, "Field is null");
		Assert.isTrue(isValidField(field), "Invalid field");

		String term = "by_" + field;

		SearchRequestBuilder search = client.prepareSearch();
		search.setTypes(Constants.ObjectType.NGINX_INDEX)
				.addAggregation(AggregationBuilders
						.terms(term)
						.field(field)
						.size(size));

		if(isValidTimeRange(startTimestamp, endTimestamp)){
			search.addAggregation(AggregationBuilders
					.range("timestamp")
					.field("@timestamp")
					.addRange(startTimestamp, endTimestamp)
			);
		}

		SearchResponse response = search.execute().actionGet();

		Terms networks = response.getAggregations().get(term);
		HashMap buckets = new HashMap<String, String>();

		for (Terms.Bucket b : networks.getBuckets()) {
			buckets.put(b.getKey(), b.getDocCount());
		}

		return buckets;
	}

//	public String networkStats(String date, Integer postId, Integer sizeInDays){
//
//	}

	public StatsJson postStats(String date, Integer postId, Integer sizeInDays){
		if (sizeInDays == null || sizeInDays == 0) sizeInDays = 30;

		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
		DateTime firstDay = dateTimeFormatter.parseDateTime(date);

		Map postReadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		postReadCounts = countPostReadByPost(postId);
		commentsCounts = countCommentByPost(postId);
		generalStatus.add(countTotals(postId, "nginx_access.postId", "nginx_access"));
		generalStatus.add(countTotals(postId, "comment.postId", "analytics"));
		generalStatus.add(countTotals(postId, "recomment.postId", "analytics"));

		TreeMap<Long, ReadsCommentsRecommendsCount> stats = makeHistogram(postReadCounts, commentsCounts, firstDay, sizeInDays);

		StatsJson response = new StatsJson();
		response.generalStatsJson = generalStatus;
		response.dateStatsJson = stats;

		return response;
	}

	public StatsJson personStats(String date, Integer sizeInDays) throws JsonProcessingException {
		Assert.notNull(date, "Invalid date. Expected yyyy-MM-dd");
		DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");

		if (sizeInDays == null || sizeInDays == 0) sizeInDays = 30;

		Person person = authService.getLoggedPerson();

		TreeMap<Long, ReadsCommentsRecommendsCount> stats = new TreeMap<>();
		DateTime firstDay = dateTimeFormatter.parseDateTime(date);

		Map postReadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		postReadCounts = countPostreadByAuthor(person.id);
		commentsCounts = countCommentByAuthor(person.id);
		generalStatus.add(countTotals(person.id, "nginx_access.authorId", "nginx_access"));
		generalStatus.add(countTotals(person.id, "comment.postAuthorId", "analytics"));
		generalStatus.add(countTotals(person.id, "recommend.postAuthorId", "analytics"));

		stats = makeHistogram(postReadCounts, commentsCounts, firstDay, sizeInDays);

		StatsJson response = new StatsJson();
		response.dateStatsJson = stats;
		response.generalStatsJson = generalStatus;

		return response;
	}

	public TreeMap<Long, ReadsCommentsRecommendsCount> makeHistogram(Map postreads, Map comments, DateTime firstDay, int size) {
		TreeMap<Long, ReadsCommentsRecommendsCount> stats = new TreeMap<>();

		DateTime lastestDay = firstDay;
		while (firstDay.minusDays(size).getMillis() < lastestDay.getMillis()) {
			ReadsCommentsRecommendsCount count = new ReadsCommentsRecommendsCount();
			if (postreads.containsKey(lastestDay.getMillis())) {
				count.readsCount = (long) postreads.get(lastestDay.getMillis());
			}

			if (comments.containsKey(lastestDay.getMillis())) {
				count.commentsCount = (long) comments.get(lastestDay.getMillis());
			}

			stats.put(lastestDay.getMillis(), count);
			lastestDay = lastestDay.minusDays(1);
		}
		return stats;
	}

	private Map generalCounter(String queryName, String indexName, QueryBuilder query, String orderField) {
		SearchRequestBuilder search = client.prepareSearch(indexName);

		search.setQuery(query);
		search.addAggregation(AggregationBuilders.dateHistogram(queryName).field(orderField).interval(DateHistogram.Interval.DAY));
//		search.addSort(SortBuilders.fieldSort("@timestamp").order(SortOrder.DESC));

		SearchResponse response = search.execute().actionGet();
		DateHistogram histogram = response.getAggregations().get(queryName);

		HashMap hist = new HashMap();

		for (DateHistogram.Bucket b : histogram.getBuckets()) {
			hist.put(b.getKeyAsDate().toDate().getTime(), b.getDocCount());
		}

		System.out.println(search.toString());

		return hist;
	}

	public Map countPostreadByAuthor(Integer authorId) {
		return generalCounter("author_read", "nginx_access", boolQuery().must(termQuery("authorId", authorId)).must(termQuery("type", "nginx_access")), "@timestamp");
	}

	public Map countCommentByPost(Integer postId) {
		return generalCounter("comments_count", "analytics", boolQuery().must(termQuery("postId", postId)), "date");
	}

	public Map<Long, Integer> countPostReadByPost(Integer postId) {
		return generalCounter("post_read", "nginx_access", boolQuery().must(termQuery("postId", postId)).must(termQuery("_type", "nginx_access")), "@timestamp");
	}

	public Map countCommentByAuthor(Integer id) {
		return generalCounter("author_comment", "analytics", boolQuery().must(termQuery("postAuthorId", id)), "date");
	}

	public int countTotals(int id, String entity, String index) {
		return (int) client.prepareSearch(index).setQuery(termQuery(entity, id)).execute().actionGet().getHits().getTotalHits();
	}
}