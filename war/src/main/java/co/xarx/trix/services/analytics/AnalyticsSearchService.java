package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.MobileStats;
import co.xarx.trix.api.v2.ReadsCommentsRecommendsCountData;
import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.domain.*;
import co.xarx.trix.util.Constants;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Predicate;

import static co.xarx.trix.util.AnalyticsUtil.getInterval;
import static co.xarx.trix.util.AnalyticsUtil.makeHistogram;

@Service
public class AnalyticsSearchService {

	private ESQueries esQueries;
	private AnalyticsQueries analyticsQueries;

	@Autowired
	public AnalyticsSearchService(ESQueries esQueries, AnalyticsQueries analyticsQueries){
		this.esQueries = esQueries;
		this.analyticsQueries = analyticsQueries;
	}

	public StatsData getPostStats(Post post, Interval interval) {
		List<Integer> generalStatus = getGeneralStatus(post);
		TreeMap<Long, ReadsCommentsRecommendsCountData> histogram = getHistogram(post, interval);

		StatsData response = new StatsData();
		response.generalStatsJson = generalStatus;
		response.dateStatsJson = histogram;

		return response;
	}

	public StatsData getPersonStats(Person person, Interval interval) {
		List<Integer> generalStatus = getGeneralStatus(person);
		TreeMap<Long, ReadsCommentsRecommendsCountData> histogram = getHistogram(person, interval);

		StatsData response = new StatsData();
		response.generalStatsJson = generalStatus;
		response.dateStatsJson = histogram;

		return response;
	}

	public StatsData getStationStats(Station station, Interval interval) {
		List<Integer> generalStatus = getGeneralStatus(station);
		TreeMap<Long, ReadsCommentsRecommendsCountData> histogram = getHistogram(station, interval);

		StatsData StatsData = new StatsData();
		StatsData.generalStatsJson = generalStatus;
		StatsData.dateStatsJson = histogram;

		return StatsData;
	}

	public StatsData getNetworkStats(Network network, Interval interval) {
		StatsData statsData = new StatsData();
		List<MobileStats> mobileStats = analyticsQueries.getMobileStats(network.getTenantId(), interval);

		MobileStats androidStats = getByType(mobileStats, Constants.MobilePlatform.ANDROID);
		MobileStats appleStats = getByType(mobileStats, Constants.MobilePlatform.APPLE);

		MobileStats fcmAndroidStatus = getByType(mobileStats, Constants.MobilePlatform.FCM_ANDROID);
		MobileStats fcmAppleStatus = getByType(mobileStats, Constants.MobilePlatform.FCM_APPLE);

		MobileStats androidStatsJoined = joinMobileStats(fcmAndroidStatus, androidStats);
		MobileStats appleStatsJoined = joinMobileStats(fcmAppleStatus, appleStats);

		statsData.generalStatsJson = getGeneralStatus(network);
		statsData.generalStatsJson.add(androidStatsJoined.currentInstallations);
		statsData.generalStatsJson.add(appleStatsJoined.currentInstallations);
		statsData.dateStatsJson = getHistogram(network, interval);

		// repeting data. Ideal: remove generalStatus list and use Key-Value
		statsData.androidStore = getByType(mobileStats, Constants.MobilePlatform.ANDROID);
		statsData.iosStore = getByType(mobileStats, Constants.MobilePlatform.APPLE);
		statsData.fileSpace = analyticsQueries.getFileStats();

		return statsData;
	}

	private MobileStats joinMobileStats(MobileStats fcmStats, MobileStats oldStats){
		MobileStats joined = new MobileStats();
		joined.currentInstallations = fcmStats.currentInstallations + oldStats.currentInstallations;
		joined.monthlyActiveUsers = fcmStats.monthlyActiveUsers = oldStats.monthlyActiveUsers;
		joined.weeklyActiveUsers = fcmStats.weeklyActiveUsers + oldStats.weeklyActiveUsers;
		joined.monthlyDownloads = fcmStats.monthlyDownloads + oldStats.monthlyDownloads;
		joined.type = oldStats.type;

		return joined;
	}

	public TreeMap<Long, ReadsCommentsRecommendsCountData> getHistogram(AnalyticsEntity entity, Interval interval){
		Map<Long, Long> reads, comments, recommends;
		reads = esQueries.getReadsByEntity(entity);
		comments = esQueries.getCommentsByEntity(entity);
		recommends = esQueries.getRecommendsByEntity(entity);

		return makeHistogram(reads, comments, recommends, interval);
	}

	public List<Integer> getGeneralStatus(AnalyticsEntity entity) {
		List<Integer> generalStatus = new ArrayList<>();

		Integer totalReads = esQueries.countActionsByEntity(Constants.StatsEventType.POST_READ, entity);
		Integer totalComments = esQueries.countActionsByEntity(Constants.StatsEventType.POST_COMMENT, entity);
		Integer totalRecommends = esQueries.countActionsByEntity(Constants.StatsEventType.POST_RECOMMEND, entity);

		generalStatus.add(totalReads); // 1st reads
		generalStatus.add(totalComments); // 2nd comments
		generalStatus.add(totalRecommends); // 3rd recommends

		return generalStatus;
	}

	public Map findMostPopular(String field, String byField, Object byValue, Interval interval, Integer size, Integer page){
		try {
			return esQueries.findMostPopular(field, byField, byValue, interval, size, page);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap();
		}
	}

	public Map<Integer, Integer> getPostReads(List<Post> posts){
		Map postReads = new HashMap();

		posts.forEach( post -> {
			postReads.put(post.getId(), esQueries.countActionsByEntity(Constants.StatsEventType.POST_READ, post));
		});

		return postReads;
	}

	private MobileStats getByType(List<MobileStats> list, Constants.MobilePlatform type){
		if(list.isEmpty()) return new MobileStats();

		Predicate<MobileStats> mobilePredicate = m -> m.getType().equals(type);
		Optional<MobileStats> optional = list.stream().filter(mobilePredicate).findFirst();
		if(optional.isPresent()) return optional.get();

		return null;
	}

	public Map getFileStats(){
		return analyticsQueries.getFileStats();
	}

	public Map<String, Integer> getReadersByStation(Station station) {
		Integer total = analyticsQueries.getPersonIdsFromStation(station.getId()).size();
		if (total == null || total == 0) return new HashMap<>();

        Interval interval = getInterval(DateTime.now(), 1);
        List<MobileStats> mobileStats = analyticsQueries.getMobileStats(station.getTenantId(), interval);

        Integer totalAndroid = getByType(mobileStats, Constants.MobilePlatform.ANDROID).currentInstallations;
        Integer totalIos = getByType(mobileStats, Constants.MobilePlatform.APPLE).currentInstallations;

		Map readers = new HashMap();
		readers.put("total", total);
		readers.put("ios", totalIos);
		readers.put("android", totalAndroid);

		return readers;
	}
}
