package co.xarx.trix.web.rest.resource;

import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.services.analytics.StatisticsService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.StatsApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@NoArgsConstructor
public class StatsResource extends AbstractResource implements StatsApi {

	@Autowired
	public StatisticsService statisticsService;

	@Override
	public StatsData postStats(String end, String start, Integer postId) throws IOException {
		return statisticsService.postStats(end, start, postId);
	}

	@Override
	public StatsData authorStats(String date, String beginning, Integer authorId) throws IOException {
		return statisticsService.authorStats(date, beginning, authorId);
	}

	@Override
	public StatsData networkStats(String date, String beginning) throws IOException {
		return statisticsService.networkStats(date, beginning);
	}

	@Override
	public StatsData stationStats(String date, String beginning, Integer stationId) throws IOException {
		return statisticsService.stationStats(date, beginning, stationId);
	}

	@Override
	public Map<String, Integer> getNetworkUsedSpace() {
		return statisticsService.getFileStats();
	}

	@Override
	public Map<Integer, Integer> countReadsByPostIds(List<Integer> postIds) {
		return statisticsService.countPostReads(postIds);
	}
}
