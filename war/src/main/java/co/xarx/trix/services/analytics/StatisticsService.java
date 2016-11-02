package co.xarx.trix.services.analytics;

import co.xarx.trix.api.v2.MobileStats;
import co.xarx.trix.api.v2.ReadsCommentsRecommendsCountData;
import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.domain.page.query.statement.StatStatement;
import co.xarx.trix.persistence.*;
import co.xarx.trix.util.Constants;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static co.xarx.trix.util.AnalyticsUtil.*;

@Service
public class StatisticsService {

	private ESQueries esQueries;
	private CommentRepository commentRepository;

	@Autowired
	public StatisticsService(ESQueries esQueries, CommentRepository commentRepository) {
		this.esQueries = esQueries;
		this.commentRepository = commentRepository;
	}

	public Map getMostPopular(StatStatement statement){
		try {
			return esQueries.findMostPopular(statement);
		} catch (Exception e) {
			e.printStackTrace();
			return new HashMap();
		}
	}

	public StatsData getSimpleStats(StatStatement statement){
		List<Integer> generalStatus = getGeneralStatus(statement);
		TreeMap<Long, ReadsCommentsRecommendsCountData> histogram = getHistogram(statement);

		StatsData response = new StatsData();
		response.generalStatsJson = generalStatus;
		response.dateStatsJson = histogram;

		return response;
	}
	
	public StatsData getNetworkStats(StatStatement statement){
		StatsData statsData = new StatsData();
		List<MobileStats> mobileStats = esQueries.getMobileStats(statement.getFieldId(), statement.getInterval().getEnd());

		MobileStats androidStats = getByType(mobileStats, Constants.MobilePlatform.ANDROID);
		MobileStats appleStats = getByType(mobileStats, Constants.MobilePlatform.APPLE);

		MobileStats fcmAndroidStatus = getByType(mobileStats, Constants.MobilePlatform.FCM_ANDROID);
		MobileStats fcmAppleStatus = getByType(mobileStats, Constants.MobilePlatform.FCM_APPLE);

		MobileStats androidStatsJoined = joinMobileStats(fcmAndroidStatus, androidStats);
		MobileStats appleStatsJoined = joinMobileStats(fcmAppleStatus, appleStats);

		statsData.generalStatsJson = getGeneralStatus(statement);
		statsData.generalStatsJson.add(androidStatsJoined.currentInstallations);
		statsData.generalStatsJson.add(appleStatsJoined.currentInstallations);
		statsData.dateStatsJson = getHistogram(statement);

		// repeting data. Ideal: remove generalStatus list and use Key-Value JSON
		statsData.androidStore = androidStatsJoined;
		statsData.iosStore = appleStatsJoined;
		statsData.fileSpace = getFileStats();

		return statsData;
	}

	public Map<Integer, Integer> getPostReads(List<Integer> postIds){
		Map postReads = new HashMap();

		postIds.forEach( id -> {
			postReads.put(id, esQueries.countTotals(id, "postId", Constants.StatsEventType.POST_READ));
		});

		return postReads;
	}

	public Map<String, Integer> getFileStats(){
        return esQueries.getFileStats();
	}

	public Map<String, Integer> getStationReaders(Integer stationId){
		Integer total = esQueries.getPersonIdsFromStation(stationId).size();
		if (total == null || total == 0) return new HashMap<>();

		Interval interval = new Interval(DateTime.now().minusDays(1), DateTime.now());
		List<MobileStats> mobileStats = esQueries.getMobileStats(TenantContextHolder.getCurrentTenantId(), interval.getEnd());

		Integer totalAndroid = getByType(mobileStats, Constants.MobilePlatform.ANDROID).currentInstallations;
		Integer totalIos = getByType(mobileStats, Constants.MobilePlatform.APPLE).currentInstallations;

		Map readers = new HashMap();
		readers.put("total", total);
		readers.put("ios", totalIos);
		readers.put("android", totalAndroid);
		readers.put("stationId", stationId);

		return readers;
	}

	public Map<String,Integer> dashboardStats() {
		Map<String, Integer> ret = new LinkedHashMap<>();
//		Long posts = postRepository.countByState(Post.STATE_PUBLISHED);
		Long comments = commentRepository.count();

//		ret.put("post", posts != null ? posts.intValue() : 0);
		ret.put("comment", comments.intValue());

		return ret;
	}

	public TreeMap<Long, ReadsCommentsRecommendsCountData> getHistogram(StatStatement statement){
		Map<Long, Long> reads, comments, recommends;
		reads = esQueries.getReadsByEntity(statement);
		comments = esQueries.getCommentsByEntity(statement);
		recommends = esQueries.getRecommendsByEntity(statement);

		return makeHistogram(reads, comments, recommends, statement.getInterval());
	}


	public List<Integer> getGeneralStatus(StatStatement statement) {
		List<Integer> generalStatus = new ArrayList<>();

		Integer totalReads = esQueries.countTotals(statement.getFieldId(), statement.getField() + "Id" , Constants.StatsEventType.POST_READ);
		Integer totalComments = esQueries.countTotals(statement.getFieldId(), statement.getField() + "Id", Constants.StatsEventType.POST_COMMENT);
		Integer totalRecommends = esQueries.countTotals(statement.getFieldId(), statement.getField() + "Id", Constants.StatsEventType.POST_RECOMMEND);

		generalStatus.add(totalReads); // 1st reads
		generalStatus.add(totalComments); // 2nd comments
		generalStatus.add(totalRecommends); // 3rd recommends

		return generalStatus;
	}
}