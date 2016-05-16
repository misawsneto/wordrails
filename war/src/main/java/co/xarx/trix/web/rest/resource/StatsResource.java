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
	public StatsData authorStats(String end, String start, Integer authorId) throws IOException {
		return statisticsService.authorStats(end, start, authorId);
	}

	@Override
	public StatsData networkStats(String end, String start) throws IOException {
		return statisticsService.networkStats(end, start);
	}

	@Override
	public StatsData stationStats(String end, String start, Integer stationId) throws IOException {
		return statisticsService.stationStats(end, start, stationId);
	}

	@Override
	public Map<String, Integer> getNetworkUsedSpace() {
		return statisticsService.getFileStats();
	}

	@Override
	public Map<Integer, Integer> countReadsByPostIds(List<Integer> postIds) {
		return statisticsService.countPostReads(postIds);
	}

	@Override
	public Map<String, Integer> countReadersByStation(Integer stationId) {
		return statisticsService.getStationReaders(stationId);
	}
}
