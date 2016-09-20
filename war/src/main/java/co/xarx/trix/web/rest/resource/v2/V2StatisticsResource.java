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
	public Response getMostPopular(Integer page,
								   Integer size,
								   String startTime,
								   String endTime,
								   String field,
								   String byField,
								   String byValue){
		if(field == null || field.isEmpty()) return badRequest("field", "must be defined");
		if(size == null || size <= 0) return badRequest("size", "must be defined and bigger than zero");

		Map map;
		map = statisticsService.getMostPopular(field, byField, byValue, startTime, endTime, size);
		return ok(map);
	}

	@Override
	public Response getPopularNetworks(Integer page, Integer size){
		Map popular;

		checkSize(size);

		popular = statisticsService.getMostPopular("tenantId", null, null, start, end, size);
		return ok(popular);
	}

	@Override
	public Response postStats(String end, String start, Integer postId) {
		checkDate(end);
		StatsData statsData = statisticsService.getPostStats(end, start, postId);
		if(statsData == null) return notFound("post");
		return ok(statsData);
	}

	@Override
	public Response authorStats(String end, String start, Integer authorId) throws IOException {
		checkDate(end);
		StatsData statsData = statisticsService.getAuthorStats(end, start, authorId);
		if(statsData == null) return notFound("author");

		return ok(statsData);
	}

	@Override
	public Response networkStats(String end, String start) throws IOException {
		checkDate(end);
		StatsData networkData = statisticsService.getNetworkStats(end, start);
		if(networkData == null) return notFound("network");

		return ok(networkData);
	}

	@Override
	public Response stationStats(String end, String start, Integer stationId) throws IOException {
		checkDate(end);
		StatsData stationData = statisticsService.getStationStats(end, start, stationId);
		if(stationData == null) return notFound("station");

		return ok(stationData);
	}

	@Override
	public Response getNetworkUsedSpace() {
		Map<String, Integer> storage = statisticsService.getFileStats();
		return ok(storage);
	}

	@Override
	public Response countReadsByPostIds(List<Integer> postIds) {
		Map<Integer, Integer> countReads = statisticsService.getPostReads(postIds);
		return ok(countReads);
	}

	@Override
	public Response countReadersByStation(Integer stationId) {
		if(stationId == null) return badRequest("stationId", "must be defined");

		Map<String, Integer> map = statisticsService.getStationReaders(stationId);

		if(map == null) return notFound("Station");
		return ok(map);
	}

	@Override
	public Response dashboardStats() {
		Map<String, Integer> dashboard = statisticsService.dashboardStats();
		return Response.status(Response.Status.OK).entity(dashboard).build();
	}

	public Response serviceUnavailable(String message){
		throw new ServiceUnavailableException(message);
	}

	public Response badRequest(String fieldName, String message){
		throw new BadRequestException(fieldName + " " + message);
	}

	public Response notFound(String fieldName){
		throw new NotFoundException(fieldName + " not found");
	}

	public Response ok(Object object){
		return Response.status(Response.Status.OK).entity(object).build();
	}

	private void checkDate(String end) {
		if(end == null || end.isEmpty()) badRequest("end", "must be a valid date");
	}
}
