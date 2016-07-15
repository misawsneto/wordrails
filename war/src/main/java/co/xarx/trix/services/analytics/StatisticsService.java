package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.api.v2.StoreStatsData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.security.PersonPermissionService;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.*;

import static co.xarx.trix.util.AnalyticsUtil.getInterval;

@Service
public class StatisticsService {

	private FileRepository fileRepository;
	private PostRepository postRepository;
	private CommentRepository commentRepository;
	private PublishedAppRepository appRepository;
	private MobileDeviceRepository mobileDeviceRepository;
	private PersonPermissionService personPermissionService;
	private PersonRepository personRepository;
	private StationRepository stationRepository;
	private NetworkRepository networkRepository;
	private AnalyticsSearchService analyticsSearchService;

	@Autowired
	public StatisticsService(MobileDeviceRepository mobileDeviceRepository, PublishedAppRepository appRepository, FileRepository fileRepository, PersonPermissionService personPermissionService, PostRepository postRepository, CommentRepository commentRepository, PersonRepository personRepository, StationRepository stationRepository, AnalyticsSearchService analyticsSearchService {
		this.appRepository = appRepository;
		this.fileRepository = fileRepository;
		this.postRepository = postRepository;
		this.commentRepository = commentRepository;
		this.mobileDeviceRepository = mobileDeviceRepository;
		this.personPermissionService = personPermissionService;
		this.personRepository = personRepository;
		this.stationRepository = stationRepository;
		this.analyticsSearchService = analyticsSearchService;
	}

	public Map getPorpularNetworks(Interval interval, Integer size) throws Exception {
		return analyticsSearchService.findMostPopular("tenantId", interval, size);
	}

	public StatsData getPostStats(String end, String beginning, Integer postId){
		Interval interval = getInterval(end, beginning);
		Post post = postRepository.findOne(postId);
		return analyticsSearchService.getPostStats(post, interval);
	}

	public StatsData getAuthorStats(String end, String start, Integer authorId) throws JsonProcessingException {
		Interval interval = getInterval(end, start);
		Person person = personRepository.findOne(authorId);

		return analyticsSearchService.getPersonStats(person, interval);
	}

	public StatsData getStationStats(String end, String start, Integer stationId){
		Interval interval = getInterval(end, start);
		Station station = stationRepository.findOne(stationId);

		return analyticsSearchService.getStationStats(station, interval);
	}

	public StatsData getNetworkStats(String end, String start){
		Interval interval = getInterval(end, start);

		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId);

		return analyticsSearchService.getNetworkStats(network, interval);
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
		List<Person> persons = personPermissionService.getPersonFromStation(stationId, Permissions.READ);

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
			if(device.type == Constants.MobilePlatform.ANDROID) {
				androidCounter++;
				continue;
			}
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

	public Map<String,Integer> dashboardStats() {
		Map<String, Integer> ret = new LinkedHashMap<>();
		Long posts = postRepository.countByState(Post.STATE_PUBLISHED);
		Long comments = commentRepository.count();

		ret.put("post", posts != null ? posts.intValue() : 0);
		ret.put("comment", comments.intValue());

		return ret;
	}
}