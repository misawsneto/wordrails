package co.xarx.trix.util;

import co.xarx.trix.api.v2.ReadsCommentsRecommendsCountData;
import co.xarx.trix.exception.BadRequestException;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Map;
import java.util.TreeMap;

public class AnalyticsUtil {

	private static int WEEK_INTERVAL = 7;
	private static int MONTH_INTERVAL = 30;
	private static DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd").withZoneUTC();

	public static Interval getInterval(DateTime endDate, Integer size) {
		if (size == null) new Interval(endDate.minusDays(MONTH_INTERVAL), endDate);
		return new Interval(endDate.minusDays(size), endDate);
	}

	public static Interval getInterval(String date, Integer size) {
		return getInterval(date != null ? dateTimeFormatter.parseDateTime(date) : new DateTime(), size);
	}

	public static Interval getInterval(String end, String start) throws BadRequestException {
		DateTime endDate = dateTimeFormatter.parseDateTime(end);

		if (start == null || start.isEmpty()) {
			return new Interval(endDate.minusDays(MONTH_INTERVAL), endDate);
		}

		DateTime startDate = dateTimeFormatter.parseDateTime(start);

		if (endDate.isAfter(startDate)) {
			return new Interval(startDate, endDate);
		}

		throw new BadRequestException("Wrong time range. 'end' must be a date after 'start'");
	}

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
}
