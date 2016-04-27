package co.xarx.trix.services.analytics;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.PublishedApp;
import co.xarx.trix.persistence.MobileDeviceRepository;
import co.xarx.trix.persistence.PublishedAppRepository;
import co.xarx.trix.util.Constants;
import co.xarx.trix.util.ReadsCommentsRecommendsCount;
import co.xarx.trix.util.StatsJson;
import co.xarx.trix.util.StoreStatsData;
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
import org.joda.time.Days;
import org.joda.time.Interval;
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
@lombok.Getter
@lombok.Setter
public class StatisticsService {

	private Client client;
	public ObjectMapper mapper;
	private String analyticsIndex;
	private DateTimeFormatter dateTimeFormatter;
	private MobileDeviceRepository mobileDeviceRepository;
	private PublishedAppRepository appRepository;

	private static int defaultInterval = 30;

	@Autowired
	public StatisticsService(Client client,
							 MobileDeviceRepository mobileDeviceRepository,
							 @Qualifier("objectMapper") ObjectMapper mapper,
							 @Value("${elasticsearch.analyticsIndex}") String analyticsIndex,
							 PublishedAppRepository appRepository){
		this.mapper = mapper;
		this.client = client;
		this.appRepository = appRepository;
		this.analyticsIndex = analyticsIndex;
		this.dateTimeFormatter = getFormatter();
		this.mobileDeviceRepository = mobileDeviceRepository;
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

	public StatsJson postStats(String date, Integer postId, Integer sizeInDays){
		Interval interval = getInterval(date, sizeInDays);

		Map postReadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		postReadCounts = countPostReadByPost(postId);
		commentsCounts = countCommentByPost(postId);
		generalStatus.add(countTotals(postId, "nginx_access.postId", "nginx_access"));
		generalStatus.add(countTotals(postId, "comment.postId", "analytics"));
		generalStatus.add(countTotals(postId, "recomment.postId", "analytics"));

		TreeMap<Long, ReadsCommentsRecommendsCount> stats = makeHistogram(postReadCounts, commentsCounts, interval);

		StatsJson response = new StatsJson();
		response.generalStatsJson = generalStatus;
		response.dateStatsJson = stats;

		return response;
	}

	public StatsJson personStats(String date, Integer personId, Integer sizeInDays) throws JsonProcessingException {
		Interval interval = getInterval(date, sizeInDays);

		Map postReadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		postReadCounts = countPostreadByAuthor(personId);
		commentsCounts = countCommentByAuthor(personId);
		generalStatus.add(countTotals(personId, "nginx_access.authorId", "nginx_access"));
		generalStatus.add(countTotals(personId, "comment.postAuthorId", "analytics"));
		generalStatus.add(countTotals(personId, "recommend.postAuthorId", "analytics"));

		StatsJson response = new StatsJson();
		response.dateStatsJson = makeHistogram(postReadCounts, commentsCounts, interval);
		response.generalStatsJson = generalStatus;

		return response;
	}

	public StatsJson networkStats(String end, String start){
		Interval interval = getInterval(end, start);

		Map postreadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		String tenantId = TenantContextHolder.getCurrentTenantId();
		Assert.hasText(tenantId, "Tenant context is required but is not available");

		postreadCounts = countPostreadByTenant(tenantId);
		commentsCounts = countCommentByTenant(tenantId);
		generalStatus.add(countTotals(tenantId, "nginx_access.tenantId", "nginx_access"));
		generalStatus.add(countTotals(tenantId, "comment.tenantId", "analytics"));
		generalStatus.add(countTotals(tenantId, "recommend.tenantId", "analytics"));
		generalStatus.add((int) (long) mobileDeviceRepository.countAndroidDevices(tenantId));
		generalStatus.add((int) (long) mobileDeviceRepository.countAppleDevices(tenantId));

		StatsJson statsJson = new StatsJson();
		statsJson.generalStatsJson = generalStatus;
		statsJson.dateStatsJson = makeHistogram(postreadCounts, commentsCounts, interval);

		statsJson.androidStore = getAndroidStats();
		statsJson.iosStore = getIosStats();

		return statsJson;
	}

	private StoreStatsData getIosStats() {
		String tenant = TenantContextHolder.getCurrentTenantId();
		PublishedApp ios = appRepository.findByTenantIdAndType(tenant, Constants.MobilePlatform.APPLE);

		return new StoreStats(ios).getStoreStats();

	}

	private StoreStatsData getAndroidStats() {
		String tenant = TenantContextHolder.getCurrentTenantId();
		PublishedApp android = appRepository.findByTenantIdAndType(tenant, Constants.MobilePlatform.ANDROID);

		return new StoreStats(android).getStoreStats();
	}

	public StatsJson stationStats(String end, String start, Integer stationId){
		Interval interval = getInterval(end, start);

		Map postreadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		postreadCounts = countPostreadByStation(stationId);
		commentsCounts = countCommentByStation(stationId);
		generalStatus.add(countTotals(stationId, "nginx_access.stationId", "nginx_access"));
		generalStatus.add(countTotals(stationId, "comment.stationId", "analytics"));
		generalStatus.add(countTotals(stationId, "recommend.stationId", "analytics"));

		StatsJson statsJson = new StatsJson();
		statsJson.generalStatsJson = generalStatus;
		statsJson.dateStatsJson = makeHistogram(postreadCounts, commentsCounts, interval);

		return statsJson;
	}

	public Interval getInterval(String end, String start){
		Assert.notNull(end, "Invalid date. Expected yyyy-MM-dd");
		DateTime endDate = dateTimeFormatter.parseDateTime(end);

		if(start != null && !start.isEmpty()){
			DateTime startDate = dateTimeFormatter.parseDateTime(start);
			Assert.isTrue(endDate.isAfter(startDate), "Wrong time range. 'beginnig' should be before 'date'");
			return new Interval(startDate, endDate);
		}

		return new Interval(endDate.minusDays(defaultInterval), endDate);
	}

	public Interval getInterval(String date, Integer size){
		Assert.notNull(date, "Invalid date. Expected yyyy-MM-dd");

		DateTime endDate = dateTimeFormatter.parseDateTime(date);

		if(size == null) new Interval(endDate.minusDays(defaultInterval), endDate);

		return new Interval(endDate.minusDays(size), endDate);
	}

	public DateTimeFormatter getFormatter(){
		return DateTimeFormat.forPattern("yyyy-MM-dd").withZoneUTC();
	}

	public TreeMap<Long, ReadsCommentsRecommendsCount> makeHistogram(Map postreads, Map comments, Interval interval) {
		TreeMap<Long, ReadsCommentsRecommendsCount> stats = new TreeMap<>();

		DateTime firstDay = interval.getEnd();
		DateTime lastestDay = firstDay;

		int size = Days.daysBetween(interval.getStart(), interval.getEnd()).getDays();

		while (firstDay.minusDays(size).isBefore(lastestDay)) {
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

		return hist;
	}

	public Map countPostreadByAuthor(Integer authorId) {
		return generalCounter("author_read_author", "nginx_access", boolQuery().must(termQuery("authorId", authorId)).must(termQuery("type", "nginx_access")), "@timestamp");
	}

	public Map countPostreadByTenant(String tenantId){
		return generalCounter("author_read_network", "nginx_access", boolQuery().must(termQuery("tenantId", tenantId)).must(termQuery("type", "nginx_access")), "@timestamp");
	}

	public Map countCommentByPost(Integer postId) {
		return generalCounter("comments_count", "analytics", boolQuery().must(termQuery("postId", postId)), "date");
	}

	public Map countCommentByTenant(String tenantId){
		return generalCounter("comments_count_tentant", "analytics", boolQuery().must(termQuery("tenantId", tenantId)), "date");
	}

	public Map<Long, Integer> countPostReadByPost(Integer postId) {
		return generalCounter("post_read", "nginx_access", boolQuery().must(termQuery("postId", postId)).must(termQuery("_type", "nginx_access")), "@timestamp");
	}

	public Map countPostreadByStation(Integer stationId){
		return generalCounter("post_read_station", "nginx_access", boolQuery().must(termQuery("stationId", stationId)).must(termQuery("_type", "nginx_access")), "@timestamp");
	}

	public Map countCommentByStation(Integer stationId) {
		return generalCounter("comment_station", "analytics", boolQuery().must(termQuery("stationId", stationId)).must(termQuery("_type", "comment")), "@timestamp");
	}

	public Map countCommentByAuthor(Integer id) {
		return generalCounter("author_comment", "analytics", boolQuery().must(termQuery("postAuthorId", id)), "date");
	}

	public Integer countTotals(Integer id, String entity, String index) {
		return (int) client.prepareSearch(index).setQuery(termQuery(entity, id)).execute().actionGet().getHits().getTotalHits();
	}

	public Integer countTotals(String id, String entity, String index) {
		return (int) client.prepareSearch(index).setQuery(termQuery(entity, id)).execute().actionGet().getHits().getTotalHits();
	}
}