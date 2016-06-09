package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.services.analytics.StatisticsService;
import co.xarx.trix.web.rest.api.v2.V2StatisticsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class V2StatisticsResource implements V2StatisticsApi {

	@Autowired
	public StatisticsService statisticsService;

	@Override
	public ContentResponse getMostCommonField(Integer page,
											 Integer size,
											 Long startTime,
											 Long endTime,
											 String field){
		ContentResponse response = new ContentResponse();
		response.content = statisticsService.findMostPopular(field, startTime, endTime, size);
		return response;
	}

	@Override
	public ContentResponse getPopularNetworks(Integer page, Integer size){
		ContentResponse response = new ContentResponse();
		response.content = statisticsService.getPorpularNetworks();
		return response;
	}

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

	@Override
	public Map<String, Integer> dashboardStats() {
		return statisticsService.dashboardStats();
	}
}
