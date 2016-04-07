package co.xarx.trix.services;

import co.xarx.trix.domain.Person;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.ReadsCommentsRecommendsCount;
import org.elasticsearch.action.count.CountRequestBuilder;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterState;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class StatisticsService {

	private Client client;
	@Value("${elasticsearch.analyticsIndex}")
	private String analyticsIndex;
	private ElasticsearchTemplate elasticsearchTemplate;
	private AuthService authService;
	private org.joda.time.format.DateTimeFormatter dateTimeFormatter;

	@Autowired
	public StatisticsService(ElasticsearchTemplate elasticsearchTemplate, Client client, AuthService authService){
		this.client = client;
		this.authService = authService;
		this.elasticsearchTemplate = elasticsearchTemplate;
		dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
	}

	public HashMap getPorpularNetworks(){
		return findMostPopular("network", null, null, 10);
	}

	public List<String> getNginxFields(){
		ClusterState cs = client.admin().cluster().prepareState().setIndices(analyticsIndex).execute().actionGet().getState();

		IndexMetaData indexMetaData = cs.getMetaData().index(analyticsIndex);
		MappingMetaData mappingMetaData = indexMetaData.mapping(Constants.ObjectType.ANALYTICS_NGINX);
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
		search.setTypes(Constants.ObjectType.ANALYTICS_NGINX)
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

	public void personStats(String date, Integer postId){
		Assert.notNull(date, "Invalid date. Expected yyyy-MM-dd");

		Person person = null;
		if (postId == null || postId == 0) {
			person = authService.getLoggedPerson();
		}

		TreeMap<Long, ReadsCommentsRecommendsCount> stats = new TreeMap<>();
		DateTime firstDay = dateTimeFormatter.parseDateTime(date);

		// create date slots
		DateTime lastestDay = firstDay;
		while (firstDay.minusDays(30).getMillis() < lastestDay.getMillis()) {
			stats.put(lastestDay.getMillis(), new ReadsCommentsRecommendsCount());
			lastestDay = lastestDay.minusDays(1);
		}

		List<Integer> postReadCounts;
		List<Integer> commentsCounts;
		List<Integer> generalStatus;

		countPostReadByPostAndDate(postId, firstDay.minusDays(30).toDate(), firstDay.toDate());

		if (person == null) {
			postReadCounts = countPostReadByPostAndDate(postId, firstDay.minusDays(30).toDate(), firstDay.toDate());
			commentsCounts = countCommentByPostAndDate(postId, firstDay.minusDays(30).toDate(), firstDay.toDate());
			generalStatus = findPostStats(postId);
		}
//		else {
//			postReadCounts = postReadRepository.countByAuthorAndDate(person.id, firstDay.minusDays(30).toDate(), firstDay.toDate());
//			commentsCounts = commentRepository.countByAuthorAndDate(person.id, firstDay.minusDays(30).toDate(), firstDay.toDate());
//			generalStatus = personRepository.findPersonStats(person.id);
//		}

		// check date and map counts
//		Iterator it = stats.entrySet().iterator();
//		checkDateAndMapCounts(postReadCounts, it);
//
//		it = stats.entrySet().iterator();
//		checkDateAndMapCounts(commentsCounts, it);
//
//		String generalStatsJson = mapper.writeValueAsString(generalStatus != null && generalStatus.size() > 0 ? generalStatus.get(0) : null);
//		String dateStatsJson = mapper.writeValueAsString(stats);
	}

	public List<Integer> countPostReadByPostAndDate(Integer postId, Date dateStart, Date dateEnd){
		CountRequestBuilder search = client.prepareCount();

		search.setTypes(Constants.ObjectType.ANALYTICS_POSTREAD);
		search.setQuery(termQuery("post_id", postId));

		CountResponse response = search.execute().actionGet();

		return null;
	}

	public List<Integer> countCommentByPostAndDate(Integer postId, Date dateStart, Date dateEnd){
		return null;
	}

	public List<Integer> findPostStats(Integer postId){
		return null;
	}

	public void checkDateAndMapCounts(List<Object[]> countList, Iterator it) {
		while (it.hasNext()) {
			Map.Entry<Long, ReadsCommentsRecommendsCount> pair = (Map.Entry<Long, ReadsCommentsRecommendsCount>) it.next();
			long key = (Long) pair.getKey();
			for (Object[] counts : countList) {
				long dateLong = ((java.sql.Date) counts[0]).getTime();
				long count = (long) counts[1];
				if (new DateTime(key).withTimeAtStartOfDay().equals(new DateTime(dateLong).withTimeAtStartOfDay()))
					pair.getValue().commentsCount = count;
			}
		}
	}
}
