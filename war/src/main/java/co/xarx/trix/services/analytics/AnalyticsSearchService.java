package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.MobileStats;
import co.xarx.trix.api.v2.ReadsCommentsRecommendsCountData;
import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.*;
import co.xarx.trix.util.Constants;
import com.fasterxml.jackson.core.JsonProcessingException;
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
		Map reads, comments;
		List<Integer> generalStatus = getGeneralStatus(post);

		reads = esQueries.getReadsByEntity(post);
		comments = analyticsQueries.getCommentsByEntity(post);

		TreeMap<Long, ReadsCommentsRecommendsCountData> stats = makeHistogram(reads, comments, interval);

		StatsData response = new StatsData();
		response.generalStatsJson = generalStatus;
		response.dateStatsJson = stats;

		return response;
	}

	public StatsData getPersonStats(Person person, Interval interval) {
		Map reads, comments;
		List<Integer> generalStatus = getGeneralStatus(person);

		reads = esQueries.getReadsByEntity(person);
		comments = analyticsQueries.getCommentsByEntity(person);

		StatsData response = new StatsData();
		response.generalStatsJson = generalStatus;
		response.dateStatsJson = makeHistogram(reads, comments, interval);

		return response;
	}

	public StatsData getStationStats(Station station, Interval interval) {
		Map reads, comments;
		List<Integer> generalStatus = getGeneralStatus(station);

		reads = esQueries.getReadsByEntity(station);
		comments = analyticsQueries.getCommentsByEntity(station);

		StatsData StatsData = new StatsData();
		StatsData.generalStatsJson = generalStatus;
		StatsData.dateStatsJson = makeHistogram(reads, comments, interval);

		return StatsData;
	}


	public StatsData getNetworkStats(Network network, Interval interval) {
		Map reads, comments;
		List<MobileStats> mobileStats = analyticsQueries.getMobileStats(network.getTenantId(), interval);

		List<Integer> generalStatus = getGeneralStatus(network);
		generalStatus.add(getByType(mobileStats, Constants.MobilePlatform.ANDROID).currentInstallations);
		generalStatus.add(getByType(mobileStats, Constants.MobilePlatform.APPLE).currentInstallations);

		reads = esQueries.getReadsByEntity(network);
		comments = analyticsQueries.getCommentsByEntity(network);

		StatsData statsData = new StatsData();
		statsData.generalStatsJson = generalStatus;
		statsData.dateStatsJson = makeHistogram(reads, comments, interval);

		statsData.androidStore = getByType(mobileStats, Constants.MobilePlatform.ANDROID);
		statsData.iosStore = getByType(mobileStats, Constants.MobilePlatform.APPLE);

		statsData.fileSpace = analyticsQueries.getFileStats();

		return statsData;
	}

	public List<Integer> getGeneralStatus(AnalyticsEntity entity) {
		List<Integer> generalStatus = new ArrayList<>();

		Integer totalReads = esQueries.countReadsByEntity(entity);
		Integer totalComments = analyticsQueries.countCommentsByEntity(entity);
		Integer totalRecommends = analyticsQueries.countRecommendsByEntity(entity);

		generalStatus.add(totalReads); // 1st reads
		generalStatus.add(totalComments); // 2nd comments
		generalStatus.add(totalRecommends); // 3rd recommends

		return generalStatus;
	}

	public Map findMostPopular(String field, Interval interval, Integer size){
		try {
			return esQueries.findMostPopular(field, interval, size);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap();
		}
	}

	public Map<Integer, Integer> getPostReads(List<Post> posts){
		Map postReads = new HashMap();

		posts.forEach( post -> {
			postReads.put(post.getId(), esQueries.countReadsByEntity(post));
		});

		return postReads;
	}

	private MobileStats getByType(List<MobileStats> list, Constants.MobilePlatform type){
		if(list.isEmpty()) return new MobileStats();

		Predicate<MobileStats> mobilePredicate = m -> m.getType().equals(type);
		return list.stream().filter(mobilePredicate).findFirst().get();
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
