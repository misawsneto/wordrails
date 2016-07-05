package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.services.analytics.StatisticsService;
import co.xarx.trix.web.rest.api.v2.V2StatisticsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.ServiceUnavailableException;
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
		if(field == null || field.isEmpty()) return badRequest("field");
		Map map;
		try {
			map = statisticsService.findMostPopular(field, startTime, endTime, size);
			return ok(map);
		} catch (Exception e) {
			e.printStackTrace();
			return badRequest("field");
		}
	}

	@Override
	public Response getPopularNetworks(Integer page, Integer size){
		Map popular;
		try {
			popular = statisticsService.getPorpularNetworks();
			return ok(popular);
		} catch (Exception e) {
			e.printStackTrace();
//			return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("There is a problem with Stats Index").build();
			throw new ServiceUnavailableException("There is a problem with Stats Index");
		}
	}

	@Override
	public Response postStats(String end, String start, Integer postId) throws IOException {
		if(end == null || end.isEmpty()) badRequest("end");

		StatsData statsData = statisticsService.postStats(end, start, postId);
		return ok(statsData);
	}

	@Override
	public Response authorStats(String end, String start, Integer authorId) throws IOException {
		if (end == null || end.isEmpty()) badRequest("end");
		StatsData statsData = statisticsService.authorStats(end, start, authorId);
		return ok(statsData);
	}

	@Override
	public Response networkStats(String end, String start) throws IOException {
		if (end == null || end.isEmpty()) badRequest("end");
		StatsData networkData = statisticsService.networkStats(end, start);
		return ok(networkData);
	}

	@Override
	public Response stationStats(String end, String start, Integer stationId) throws IOException {
		if (end == null || end.isEmpty()) badRequest("end");
		StatsData stationData = statisticsService.stationStats(end, start, stationId);
		return ok(stationData);
	}

	@Override
	public Response getNetworkUsedSpace() {
		Map<String, Integer> storage = statisticsService.getFileStats();
		return ok(storage);
	}

	@Override
	public Response countReadsByPostIds(List<Integer> postIds) {
		Map<Integer, Integer> countReads = statisticsService.countPostReads(postIds);
		return ok(countReads);
	}

	@Override
	public Response countReadersByStation(Integer stationId) {
		if(stationId == null) return badRequest("stationId");

		Map<String, Integer> map = statisticsService.getStationReaders(stationId);

		if(map == null) return notFound("Station");
		return ok(map);
	}

	@Override
	public Response dashboardStats() {
		Map<String, Integer> dashboard = statisticsService.dashboardStats();
		return Response.status(Response.Status.OK).entity(dashboard).build();
	}

	public Response badRequest(String fieldName){
//		return Response.status(Response.Status.BAD_REQUEST).entity(fieldName + " must be defined").build();
		throw new BadRequestException(fieldName + " must be defined");
	}

	public Response notFound(String fieldName){
//		return Response.status(Response.Status.NOT_FOUND).entity(fieldName + " not found").build();
		throw new NotFoundException(fieldName + " must be defined");
	}

	public Response ok(Object object){
		return Response.status(Response.Status.OK).entity(object).build();
	}
}
