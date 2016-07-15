package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.ReadsCommentsRecommendsCountData;
import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

	public StatsData getPersonStats(Person person, Interval interval) throws JsonProcessingException {
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
		List<Integer> generalStatus = new ArrayList<>();

		reads = (tenantId);
		comments = countCommentByTenant(tenantId);

		generalStatus.add(countTotals(tenantId, "nginx_access.tenantId", nginxAccessIndex));
		generalStatus.add(countTotals(tenantId, "comment.tenantId", analyticsIndex));
		generalStatus.add(countTotals(tenantId, "recommend.tenantId", analyticsIndex));

		generalStatus.add((int) (long) mobileDeviceRepository.countAndroidDevices(tenantId));
		generalStatus.add((int) (long) mobileDeviceRepository.countAppleDevices(tenantId));

		StatsData statsData = new StatsData();
		statsData.generalStatsJson = generalStatus;
		statsData.dateStatsJson = makeHistogram(reads, comments, interval);

		statsData.androidStore = getAndroidStats(interval);
		statsData.iosStore = getIosStats(interval);

		statsData.fileSpace = getFileStats();

		return statsData;
	}

	public List<Integer> getGeneralStatus(AnalyticsEntity entity) {
		List<Integer> generalStatus = new ArrayList<>();

		Integer totalComments = analyticsQueries.countCommentsByEntity(entity);
		Integer totalReads = esQueries.countReadsByEntity(entity);
		Integer totalRecommends = analyticsQueries.countCommentsByEntity(entity);

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
			return null;
		}
	}
}
