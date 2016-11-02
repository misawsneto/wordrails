package co.xarx.trix.util;

import co.xarx.trix.api.v2.MobileStats;
import co.xarx.trix.api.v2.ReadsCommentsRecommendsCountData;
import co.xarx.trix.exception.BadRequestException;
import com.mysema.commons.lang.Assert;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Predicate;

public class AnalyticsUtil {

	public static TreeMap<Long, ReadsCommentsRecommendsCountData> makeHistogram(Map<Long, Long> postReads, Map<Long, Long> comments, Map<Long, Long> recommends, Interval interval) {
		TreeMap<Long, ReadsCommentsRecommendsCountData> stats = new TreeMap<>();

		DateTime firstDay = interval.getEnd();
		DateTime lastestDay = firstDay;

		int size = Days.daysBetween(interval.getStart(), interval.getEnd()).getDays();

		do {
			ReadsCommentsRecommendsCountData count = new ReadsCommentsRecommendsCountData();
			if (postReads.containsKey(lastestDay.getMillis())) {
				count.readsCount = postReads.get(lastestDay.getMillis()).intValue();
			}

			if (comments.containsKey(lastestDay.getMillis())) {
				count.commentsCount = comments.get(lastestDay.getMillis());
			}

			if(recommends.containsKey(lastestDay.getMillis())){
				count.recommendsCount = recommends.get(lastestDay.getMillis());
			}

			stats.put(lastestDay.getMillis(), count);
			lastestDay = lastestDay.minusDays(1);
		} while(firstDay.minusDays(size).isBefore(lastestDay));

		return stats;
	}

	public static MobileStats joinMobileStats(MobileStats fcmStats, MobileStats oldStats){
		MobileStats joined = new MobileStats();
		joined.currentInstallations = fcmStats.currentInstallations + oldStats.currentInstallations;
		joined.monthlyActiveUsers = fcmStats.monthlyActiveUsers = oldStats.monthlyActiveUsers;
		joined.weeklyActiveUsers = (fcmStats.weeklyActiveUsers == null? 0 : fcmStats.weeklyActiveUsers) + oldStats.weeklyActiveUsers;
		joined.monthlyDownloads = (fcmStats.monthlyDownloads == null ? 0 : fcmStats.monthlyDownloads) + oldStats
				.monthlyDownloads;
		joined.type = oldStats.type;

		return joined;
	}

	public static MobileStats getByType(List<MobileStats> list, Constants.MobilePlatform type){
		if(list.isEmpty()) return new MobileStats();

		Predicate<MobileStats> mobilePredicate = m -> m.getType().equals(type);
		Optional<MobileStats> optional = list.stream().filter(mobilePredicate).findFirst();
		if(optional.isPresent()) return optional.get();

		return null;
	}

}
