package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.ReadsCommentsRecommendsCountData;
import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.api.v2.StoreStatsData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.security.PersonPermissionService;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;

@Service
public class StatisticsService {

	private Client client;
	private String profile;
	private String trixIndex;
	private String analyticsIndex;
	private String nginxAccessIndex;
	private FileRepository fileRepository;
	private PostRepository postRepository;
	private DateTimeFormatter dateTimeFormatter;
	private PublishedAppRepository appRepository;
	private MobileDeviceRepository mobileDeviceRepository;
	private PersonPermissionService personPermissionService;
	private CommentRepository commentRepository;

	private static int MONTH_INTERVAL = 30;
	private static int WEEK_INTERVAL = 7;
	private static String ACCESS_TYPE = "nginx_access";

	@Autowired
	public StatisticsService(Client client,
							 MobileDeviceRepository mobileDeviceRepository,
							 @Value("${elasticsearch.analyticsIndex}") String analyticsIndex,
							 @Value("${elasticsearch.nginxAccessIndex}") String nginxAccessIndex,
							 @Value("${spring.data.elasticsearch.index}") String trixIndex,
							 @Value("${spring.profiles.active:dev}") String profile,
							 PublishedAppRepository appRepository,
							 FileRepository fileRepository,
							 PersonPermissionService personPermissionService,
							 PostRepository postRepository,
							 CommentRepository commentRepository){
		this.client = client;
		this.trixIndex = trixIndex;
		this.profile = profile;
		this.appRepository = appRepository;
		this.fileRepository = fileRepository;
		this.analyticsIndex = analyticsIndex;
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.dateTimeFormatter = getFormatter();
		this.nginxAccessIndex = nginxAccessIndex;
		this.mobileDeviceRepository = mobileDeviceRepository;
		this.personPermissionService = personPermissionService;
	}

	@PostConstruct
	public void init() throws ExecutionException, InterruptedException {

		if(profile.equalsIgnoreCase("prod")) {
			boolean isThereNginxIndex = client.admin().indices().exists(new IndicesExistsRequest(nginxAccessIndex)).get().isExists();
			boolean isThereAnalyticsIndex = client.admin().indices().exists(new IndicesExistsRequest(analyticsIndex)).get().isExists();

			Assert.isTrue(isThereNginxIndex, "Big problem! Index is not there: " + nginxAccessIndex);
			Assert.isTrue(isThereAnalyticsIndex, "Big problem! Index is not there: " + analyticsIndex);
		}
	}

	public Map getPorpularNetworks() throws Exception {
		return findMostPopular("tenantId", null, null, 10);
	}

	public List<String> getNginxFields() throws Exception {
		ClusterState cs = client.admin().cluster().prepareState().setIndices(nginxAccessIndex).execute().actionGet().getState();

		IndexMetaData indexMetaData = cs.getMetaData().index(nginxAccessIndex);
		if (indexMetaData == null) {
			throw new Exception("The data cannot be retrived: No index metadata");
		}

		MappingMetaData mappingMetaData = indexMetaData.mapping(ACCESS_TYPE);
		Map<String, Object> map = null;

		try {
			map = mappingMetaData.getSourceAsMap();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return getIndexFields("", map);
	}

	private  List<String> getIndexFields(String fieldName, Map<String, Object> mapProperties) {
		List<String> fieldList = new ArrayList<String>();
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

	private boolean isValidField(String field) throws Exception {
		if(getNginxFields().contains(field)) {
			return true;
		} else return false;
	}

	public boolean isValidTimeRange(Long begin, Long end){
		return begin != null && end != null && begin < end;
	}

	public HashMap findMostPopular(String field, Long startTimestamp, Long endTimestamp, Integer size) throws Exception {
		if(!isValidField(field)) {
			throw new Exception("Invalid field in nginx: " + field);
		}

		String term = "by_" + field;

		SearchRequestBuilder search = client.prepareSearch();
		search.setTypes(ACCESS_TYPE)
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

	public StatsData postStats(String end, String beginning, Integer postId){
		Interval interval = getInterval(end, beginning);
		return postStats(end, postId, Days.daysBetween(interval.getStart(), interval.getEnd()).getDays());
	}

	public StatsData postStats(String date, Integer postId, Integer sizeInDays){
		Interval interval = getInterval(date, sizeInDays);

		Map postReadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		postReadCounts = countPostReadByPost(postId);
		commentsCounts = countCommentByPost(postId);
		generalStatus.add(countTotals(postId, "nginx_access.postId", nginxAccessIndex));
		generalStatus.add(countTotals(postId, "comment.postId", analyticsIndex));
		generalStatus.add(countTotals(postId, "recommend.postId", analyticsIndex));

		TreeMap<Long, ReadsCommentsRecommendsCountData> stats = makeHistogram(postReadCounts, commentsCounts, interval);

		StatsData response = new StatsData();
		response.generalStatsJson = generalStatus;
		response.dateStatsJson = stats;

		return response;
	}

	public StatsData authorStats(String end, String start, Integer authorId) throws JsonProcessingException {
		Interval interval = getInterval(end, start);
		return personStats(end, authorId, Days.daysBetween(interval.getStart(), interval.getEnd()).getDays());
	}

	public StatsData personStats(String date, Integer personId, Integer sizeInDays) throws JsonProcessingException {
		Interval interval = getInterval(date, sizeInDays);

		Map postReadCounts, commentsCounts;
		List<Integer> generalStatus = new ArrayList<>();

		postReadCounts = countPostreadByAuthor(personId);
		commentsCounts = countCommentByAuthor(personId);
		generalStatus.add(countTotals(personId, "nginx_access.authorId", nginxAccessIndex));
		generalStatus.add(countTotals(personId, "comment.postAuthorId", analyticsIndex));
		generalStatus.add(countTotals(personId, "recommend.postAuthorId", analyticsIndex));

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
		generalStatus.add(countTotals(tenantId, "comment.tenantId", analyticsIndex));
		generalStatus.add(countTotals(tenantId, "recommend.tenantId", analyticsIndex));
		generalStatus.add((int) (long) mobileDeviceRepository.countAndroidDevices(tenantId));
		generalStatus.add((int) (long) mobileDeviceRepository.countAppleDevices(tenantId));

		StatsData statsData = new StatsData();
		statsData.generalStatsJson = generalStatus;
		statsData.dateStatsJson = makeHistogram(postreadCounts, commentsCounts, interval);

		statsData.androidStore = getAndroidStats(interval);
		statsData.iosStore = getIosStats(interval);

		statsData.fileSpace = getFileStats();

		return statsData;
	}

	public Map<Integer, Integer> countPostReads(List<Integer> postIds){
		Map postReads = new HashMap();
		postIds.forEach( postId -> postReads.put(postId, countTotals(postId, "nginx_access.postId", nginxAccessIndex)));
		return postReads;
	}

	public Map<String, Integer> getFileStats(){
		List<Object[]> mimeSums = fileRepository.sumFilesSizeByMime(TenantContextHolder.getCurrentTenantId());
		Map<String, Integer> map = new HashMap<>();

		mimeSums.stream().filter(tuple -> tuple[0] != null && tuple[1] != null)
				.forEach(tuple -> map.put((String) tuple[0], (int) (long) tuple[1]));

		return map;
	}

	public StoreStatsData getIosStats(Interval interval) {
		String tenant = TenantContextHolder.getCurrentTenantId();
		PublishedApp ios = appRepository.findByTenantIdAndType(tenant, Constants.MobilePlatform.APPLE);

		if(ios != null)
			return getAppStats(ios, interval);
		else
			return null;

	}

	public StoreStatsData getAndroidStats(Interval interval) {
		String tenant = TenantContextHolder.getCurrentTenantId();
		PublishedApp android = appRepository.findByTenantIdAndType(tenant, Constants.MobilePlatform.ANDROID);
		if(android != null)
			return getAppStats(android, interval);
		else
			return  null;
	}

	public Map getStationReaders(Integer stationId){
		List<Person> persons = personPermissionService.getPersonFromStation(stationId);

		if (persons == null || persons.size() == 0) return null;

		List<Integer> ids = new ArrayList<>();
		persons.forEach(person -> ids.add(person.id));

		List<MobileDevice> mobileDevices = mobileDeviceRepository.findByPersonIds(ids);
		Map<String, Integer> stationReaders = new HashMap<>();

		stationReaders.put("total", ids.size());
		stationReaders.put("stationId", stationId);

		int androidCounter = 0;
		int iosCounter = 0;
		for(MobileDevice device: mobileDevices){
			if(device.type == Constants.MobilePlatform.ANDROID) androidCounter++;
			if(device.type == Constants.MobilePlatform.APPLE) iosCounter++;
		}

		stationReaders.put("ios", androidCounter);
		stationReaders.put("android", iosCounter);

		return stationReaders;
	}

	public StoreStatsData getAppStats(PublishedApp app, Interval interval){
		Assert.notNull(app, "App cannot be null");

		StoreStatsData appStats = new StoreStatsData();

		Interval week = getInterval(interval.getEnd(), WEEK_INTERVAL);
		Interval month = getInterval(interval.getEnd(), MONTH_INTERVAL);

		appStats.weeklyActiveUsers = getActiveUserByInterval(week, app.getType());
		appStats.monthlyActiveUsers = getActiveUserByInterval(month, app.getType());

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
		generalStatus.add(countTotals(stationId, "comment.stationId", analyticsIndex));
		generalStatus.add(countTotals(stationId, "recommend.stationId", analyticsIndex));

		StatsData StatsData = new StatsData();
		StatsData.generalStatsJson = generalStatus;
		StatsData.dateStatsJson = makeHistogram(postreadCounts, commentsCounts, interval);

		return StatsData;
	}

	public Interval getInterval(String end, String start){
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
		return getInterval(date != null ? dateTimeFormatter.parseDateTime(date) : new DateTime(), size);
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

		SearchResponse response = search.execute().actionGet();
		DateHistogram histogram = response.getAggregations().get(queryName);

		HashMap hist = new HashMap();

		for (DateHistogram.Bucket b : histogram.getBuckets()) {
			hist.put(b.getKeyAsDate().toDate().getTime(), b.getDocCount());
		}

		return hist;
	}

	public Map countPostreadByAuthor(Integer authorId) {
		return generalCounter("author_read_author", nginxAccessIndex, boolQuery().must(termQuery("authorId", authorId)).must(termQuery("_type", ACCESS_TYPE)), "@timestamp");
	}

	public Map countPostreadByTenant(String tenantId){
		return generalCounter("author_read_network", nginxAccessIndex, boolQuery().must(termQuery("tenantId", tenantId)).must(termQuery("_type", ACCESS_TYPE)), "@timestamp");
	}

	public Map countCommentByPost(Integer postId) {
		return generalCounter("comments_count", analyticsIndex, boolQuery().must(termQuery("postId", postId)), "date");
	}

	public Map countCommentByTenant(String tenantId){
		return generalCounter("comments_count_tentant", analyticsIndex, boolQuery().must(termQuery("tenantId", tenantId)), "date");
	}

	public Map<Long, Integer> countPostReadByPost(Integer postId) {
		return generalCounter("post_read", nginxAccessIndex, boolQuery().must(termQuery("postId", postId)).must(termQuery("_type", ACCESS_TYPE)), "@timestamp");
	}

	public Map countPostreadByStation(Integer stationId){
		return generalCounter("post_read_station", nginxAccessIndex, boolQuery().must(termQuery("stationId", stationId)).must(termQuery("_type", ACCESS_TYPE)), "@timestamp");
	}

	public Map countCommentByStation(Integer stationId) {
		return generalCounter("comment_station", analyticsIndex, boolQuery().must(termQuery("stationId", stationId)).must(termQuery("_type", "comment")), "@timestamp");
	}

	public Map countCommentByAuthor(Integer id) {
		return generalCounter("author_comment", analyticsIndex, boolQuery().must(termQuery("postAuthorId", id)), "date");
	}

	public Integer countTotals(Integer id, String entity, String index) {
		return (int) client.prepareSearch(index).setQuery(boolQuery()
						.must(termQuery(entity, id))
						.must(termQuery("verb", "get"))).execute().actionGet().getHits().getTotalHits();
	}

	public Integer countTotals(String id, String entity, String index) {
		return (int) client.prepareSearch(index).setQuery(boolQuery().must(termQuery(entity, id)).must(termQuery("verb", "get"))).execute().actionGet().getHits().getTotalHits();
	}

	public Map<String,Integer> dashboardStats() {
		Map<String, Integer> ret = new LinkedHashMap<>();
		Long posts = postRepository.countByState(Post.STATE_PUBLISHED);
		Long comments = commentRepository.count();

		ret.put("post", posts != null ? posts.intValue() : 0);
		ret.put("comment", comments.intValue());

		return ret;
	}
}