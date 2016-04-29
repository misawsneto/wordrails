package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.PublishedApp;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.MobileDeviceRepository;
import co.xarx.trix.persistence.PublishedAppRepository;
import co.xarx.trix.util.Constants;
import co.xarx.trix.api.v2.ReadsCommentsRecommendsCountData;
import co.xarx.trix.api.v2.StoreStatsData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.arcadiaconsulting.appstoresstats.android.console.AndroidStoreStats;
import es.arcadiaconsulting.appstoresstats.common.CommonStatsData;
import es.arcadiaconsulting.appstoresstats.ios.console.IOSStoreStats;
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
	private ObjectMapper mapper;
	private String analyticsIndex;
	private String nginxAccessIndex;
	private DateTimeFormatter dateTimeFormatter;
	private PublishedAppRepository appRepository;
	private MobileDeviceRepository mobileDeviceRepository;
	private FileRepository fileRepository;

	private static int MONTH_INTERVAL = 30;
	private static int WEEK_INTERVAL = 7;

	@Autowired
	public StatisticsService(Client client,
							 MobileDeviceRepository mobileDeviceRepository,
							 @Qualifier("objectMapper") ObjectMapper mapper,
							 @Value("${elasticsearch.analyticsIndex}") String analyticsIndex,
							 @Value("${elasticsearch.nginxAccessIndex}") String nginxAccessIndex,
							 PublishedAppRepository appRepository, FileRepository fileRepository){
		this.mapper = mapper;
		this.client = client;
		this.appRepository = appRepository;
		this.analyticsIndex = analyticsIndex;
		this.dateTimeFormatter = getFormatter();
		this.mobileDeviceRepository = mobileDeviceRepository;
		this.fileRepository = fileRepository;
		this.nginxAccessIndex = nginxAccessIndex;
	}

	public HashMap getPorpularNetworks(){
		return findMostPopular("network", null, null, 10);
	}

	public List<String> getNginxFields(){
		ClusterState cs = client.admin().cluster().prepareState().setIndices(analyticsIndex).execute().actionGet().getState();

		IndexMetaData indexMetaData = cs.getMetaData().index(analyticsIndex);
		MappingMetaData mappingMetaData = indexMetaData.mapping(nginxAccessIndex);
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
		search.setTypes(nginxAccessIndex)
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

	public StatsData postStats(String date, Integer postId, Integer sizeInDays){
		Interval interval = getInterval(date, sizeInDays);

		Map postReadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		postReadCounts = countPostReadByPost(postId);
		commentsCounts = countCommentByPost(postId);
		generalStatus.add(countTotals(postId, "nginx_access.postId", nginxAccessIndex));
		generalStatus.add(countTotals(postId, "comment.postId", "analytics"));
		generalStatus.add(countTotals(postId, "recomment.postId", "analytics"));

		TreeMap<Long, ReadsCommentsRecommendsCountData> stats = makeHistogram(postReadCounts, commentsCounts, interval);

		StatsData response = new StatsData();
		response.generalStatsJson = generalStatus;
		response.dateStatsJson = stats;

		return response;
	}

	public StatsData personStats(String date, Integer personId, Integer sizeInDays) throws JsonProcessingException {
		Interval interval = getInterval(date, sizeInDays);

		Map postReadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		postReadCounts = countPostreadByAuthor(personId);
		commentsCounts = countCommentByAuthor(personId);
		generalStatus.add(countTotals(personId, "nginx_access.authorId", nginxAccessIndex));
		generalStatus.add(countTotals(personId, "comment.postAuthorId", "analytics"));
		generalStatus.add(countTotals(personId, "recommend.postAuthorId", "analytics"));

		StatsData response = new StatsData();
		response.dateStatsJson = makeHistogram(postReadCounts, commentsCounts, interval);
		response.generalStatsJson = generalStatus;

		return response;
	}

	public StatsData networkStats(String end, String start){
		Interval interval = getInterval(end, start);

		Map postreadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		String tenantId = TenantContextHolder.getCurrentTenantId();
		Assert.hasText(tenantId, "Tenant context is required but is not available");

		postreadCounts = countPostreadByTenant(tenantId);
		commentsCounts = countCommentByTenant(tenantId);
		generalStatus.add(countTotals(tenantId, "nginx_access.tenantId", nginxAccessIndex));
		generalStatus.add(countTotals(tenantId, "comment.tenantId", "analytics"));
		generalStatus.add(countTotals(tenantId, "recommend.tenantId", "analytics"));
		generalStatus.add((int) (long) mobileDeviceRepository.countAndroidDevices(tenantId));
		generalStatus.add((int) (long) mobileDeviceRepository.countAppleDevices(tenantId));

		StatsData StatsData = new StatsData();
		StatsData.generalStatsJson = generalStatus;
		StatsData.dateStatsJson = makeHistogram(postreadCounts, commentsCounts, interval);

		StatsData.androidStore = getAndroidStats(interval);
		StatsData.iosStore = getIosStats(interval);

		StatsData.fileSpace = getFileStats();

		return StatsData;
	}

	public Map getFileStats(){
		List<Object[]> mimeSums = fileRepository.sumFilesSizeByMime(TenantContextHolder.getCurrentTenantId());
		Map map = new HashMap<>();

		mimeSums.stream().filter(tuple -> tuple[0] != null && tuple[1] != null)
				.forEach(tuple -> map.put((String) tuple[0], (int) (long) tuple[1]));

		return map;
	}

	public StoreStatsData getIosStats(Interval interval) {
		String tenant = TenantContextHolder.getCurrentTenantId();
		PublishedApp ios = appRepository.findByTenantIdAndType(tenant, Constants.MobilePlatform.APPLE);

		return getAppStats(ios, interval);

	}

	public StoreStatsData getAndroidStats(Interval interval) {
		String tenant = TenantContextHolder.getCurrentTenantId();
		PublishedApp android = appRepository.findByTenantIdAndType(tenant, Constants.MobilePlatform.ANDROID);

		return getAppStats(android, interval);
	}

	public StoreStatsData getAppStats(PublishedApp publishedApp, Interval interval){
		CommonStatsData stats;
//		CommonStatsData periodStats;
//		CommonStatsData android = fetchAndroid.getFullStatsForApp("mobile@xarx.co", "X@rxM0b!l3", "com.wordrails.sportclubdorecife", null, "XARX");
//		this.ios = fetchIOs.getFullStatsForApp("ac@adrielcafe.com", "X@rxtr1x", "SPORTCLUBDORECIFE", "86672524", null);

		if (publishedApp.getType().equals(Constants.MobilePlatform.ANDROID)) {
			AndroidStoreStats fetchAndroid = new AndroidStoreStats();
			stats = fetchAndroid.getFullStatsForApp(
					publishedApp.getPublisherEmail(),
					publishedApp.getPublisherPassword(),
					publishedApp.getPackageName(), null,
					publishedApp.getPublisherPublicName());

//			periodStats = fetchAndroid.getStatsForApp(
//					publishedApp.getPublisherEmail(),
//					publishedApp.getPublisherPassword(),
//					publishedApp.getPackageName(),
//					interval.getStart().toDate(),
//					interval.getEnd().toDate(), null,
//					publishedApp.getPublisherPublicName());
		} else {
			IOSStoreStats fetchIos = new IOSStoreStats();
			stats = fetchIos.getFullStatsForApp(
					publishedApp.getPublisherEmail(),
					publishedApp.getPublisherPassword(),
					publishedApp.getSku(),
					publishedApp.getVendorId(), null);

//			periodStats = fetchIos.getStatsForApp(
//					publishedApp.getPublisherEmail(),
//					publishedApp.getPublisherPassword(),
//					publishedApp.getPackageName(),
//					interval.getStart().toDate(),
//					interval.getEnd().toDate(),
//					publishedApp.getVendorId(), null);
		}

		StoreStatsData appStats = new StoreStatsData();
		appStats.averageRaiting = stats.getAverageRate();
		appStats.downloads = stats.getDownloadsNumber();
		appStats.currentInstallations = stats.getCurrentInstallationsNumber();
//		appStats.monthlyDownloads = periodStats.getDownloadsNumber();

		Interval week = getInterval(interval.getEnd(), WEEK_INTERVAL);
		Interval month = getInterval(interval.getEnd(), MONTH_INTERVAL);

		appStats.weeklyActiveUsers = getActiveUserByInterval(week, publishedApp.getType());
		appStats.monthlyActiveUsers = getActiveUserByInterval(month, publishedApp.getType());

		return appStats;
	}

	public Integer getActiveUserByInterval(Interval interval, Constants.MobilePlatform type){
		return (int) (long) mobileDeviceRepository.countActiveDevices(
				TenantContextHolder.getCurrentTenantId(),
				type, interval.getStart().toString(),
				interval.getEnd().toString());
	}

	public StatsData stationStats(String end, String start, Integer stationId){
		Interval interval = getInterval(end, start);

		Map postreadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		postreadCounts = countPostreadByStation(stationId);
		commentsCounts = countCommentByStation(stationId);
		generalStatus.add(countTotals(stationId, "nginx_access.stationId", nginxAccessIndex));
		generalStatus.add(countTotals(stationId, "comment.stationId", "analytics"));
		generalStatus.add(countTotals(stationId, "recommend.stationId", "analytics"));

		StatsData StatsData = new StatsData();
		StatsData.generalStatsJson = generalStatus;
		StatsData.dateStatsJson = makeHistogram(postreadCounts, commentsCounts, interval);

		return StatsData;
	}

	public Interval getInterval(String end, String start){
		Assert.notNull(end, "Invalid date. Expected yyyy-MM-dd");
		DateTime endDate = dateTimeFormatter.parseDateTime(end);

		if(start != null && !start.isEmpty()){
			DateTime startDate = dateTimeFormatter.parseDateTime(start);
			Assert.isTrue(endDate.isAfter(startDate), "Wrong time range. 'beginnig' should be before 'date'");
			return new Interval(startDate, endDate);
		}

		return new Interval(endDate.minusDays(MONTH_INTERVAL), endDate);
	}

	public Interval getInterval(DateTime endDate, Integer size) {
		if (size == null) new Interval(endDate.minusDays(MONTH_INTERVAL), endDate);
		return new Interval(endDate.minusDays(size), endDate);
	}

	public Interval getInterval(String date, Integer size){
		Assert.notNull(date, "Invalid date. Expected yyyy-MM-dd");
		DateTime endDate = dateTimeFormatter.parseDateTime(date);

		return new Interval(endDate.minusDays(size != null ? size : MONTH_INTERVAL), endDate);
	}

	public DateTimeFormatter getFormatter(){
		return DateTimeFormat.forPattern("yyyy-MM-dd").withZoneUTC();
	}

	public TreeMap<Long, ReadsCommentsRecommendsCountData> makeHistogram(Map postreads, Map comments, Interval interval) {
		TreeMap<Long, ReadsCommentsRecommendsCountData> stats = new TreeMap<>();

		DateTime firstDay = interval.getEnd();
		DateTime lastestDay = firstDay;

		int size = Days.daysBetween(interval.getStart(), interval.getEnd()).getDays();

		while (firstDay.minusDays(size).isBefore(lastestDay)) {
			ReadsCommentsRecommendsCountData count = new ReadsCommentsRecommendsCountData();
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
		return generalCounter("author_read_author", nginxAccessIndex, boolQuery().must(termQuery("authorId", authorId)).must(termQuery("type", "nginx_access")), "@timestamp");
	}

	public Map countPostreadByTenant(String tenantId){
		return generalCounter("author_read_network", nginxAccessIndex, boolQuery().must(termQuery("tenantId", tenantId)).must(termQuery("type", "nginx_access")), "@timestamp");
	}

	public Map countCommentByPost(Integer postId) {
		return generalCounter("comments_count", "analytics", boolQuery().must(termQuery("postId", postId)), "date");
	}

	public Map countCommentByTenant(String tenantId){
		return generalCounter("comments_count_tentant", "analytics", boolQuery().must(termQuery("tenantId", tenantId)), "date");
	}

	public Map<Long, Integer> countPostReadByPost(Integer postId) {
		return generalCounter("post_read", nginxAccessIndex, boolQuery().must(termQuery("postId", postId)).must(termQuery("_type", "nginx_access")), "@timestamp");
	}

	public Map countPostreadByStation(Integer stationId){
		return generalCounter("post_read_station", nginxAccessIndex, boolQuery().must(termQuery("stationId", stationId)).must(termQuery("_type", "nginx_access")), "@timestamp");
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