package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.services.analytics.StatisticsService;
import co.xarx.trix.web.rest.api.v2.V2StatisticsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class V2StatisticsResource implements V2StatisticsApi {

	@Autowired
	public StatisticsService statisticsService;

	@Override
	public Response getMostCommonField(Integer page,
											 Integer size,
											 Long startTime,
											 Long endTime,
											 String field){
		Map map = statisticsService.findMostPopular(field, startTime, endTime, size);
		return Response.status(Response.Status.OK).entity(map).build();
	}

	@Override
	public Response getPopularNetworks(Integer page, Integer size){
		Map popular = statisticsService.getPorpularNetworks();
		return Response.status(Response.Status.OK).entity(popular).build();
	}

	@Override
	public Response postStats(String end, String start, Integer postId) throws IOException {
		StatsData statsData = statisticsService.postStats(end, start, postId);
		return Response.status(Response.Status.OK).entity(statsData).build();
	}

	@Override
	public Response authorStats(String end, String start, Integer authorId) throws IOException {
		StatsData statsData = statisticsService.authorStats(end, start, authorId);
		return Response.status(Response.Status.OK).entity(statsData).build();
	}

	@Override
	public Response networkStats(String end, String start) throws IOException {
		StatsData networkData = statisticsService.networkStats(end, start);
		return Response.status(Response.Status.OK).entity(networkData).build();
	}

	@Override
	public Response stationStats(String end, String start, Integer stationId) throws IOException {
		StatsData stationData = statisticsService.stationStats(end, start, stationId);
		return Response.status(Response.Status.OK).entity(stationData).build();
	}

	@Override
	public Response getNetworkUsedSpace() {
		Map<String, Integer> storage = statisticsService.getFileStats();
		return Response.status(Response.Status.OK).entity(storage).build();
	}

	@Override
	public Response countReadsByPostIds(List<Integer> postIds) {
		Map<Integer, Integer> countReads = statisticsService.countPostReads(postIds);
		return Response.status(Response.Status.OK).entity(countReads).build();
	}

	@Override
	public Response countReadersByStation(Integer stationId) {
		if(stationId == null){
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Map<String, Integer> map = statisticsService.getStationReaders(stationId);

		if(map == null){
			return Response.status(Response.Status.NOT_FOUND).entity(map).build();
		}

		return Response.status(Response.Status.OK).entity(map).build();
	}

	@Override
	public Response dashboardStats() {
		Map<String, Integer> dashboard = statisticsService.dashboardStats();
		return Response.status(Response.Status.OK).entity(dashboard).build();
	}
}
