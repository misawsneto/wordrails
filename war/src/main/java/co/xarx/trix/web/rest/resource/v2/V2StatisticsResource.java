package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.StatsData;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.page.query.statement.StatStatement;
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
								   List<String> byFields,
								   List<String> byValues){
		if(field == null || field.isEmpty()) return badRequest("field", "must be defined");
		checkSize(size);

		Map map;
		StatStatement statement = new StatStatement(field, byFields, byValues, startTime, endTime, size, page);
		map = statisticsService.getMostPopular(statement);
		return ok(map);
	}

	@Override
	public Response getPopularNetworks(Integer page, Integer size, String start, String end){
		StatStatement statement = new StatStatement("tenant", TenantContextHolder.getCurrentTenantId(), start, end);
		return ok(statisticsService.getMostPopular(statement));
	}

	@Override
	public Response postStats(String end, String start, Integer postId) {
		StatStatement statement = new StatStatement("post", postId, start, end);

		checkDate(end);
		StatsData statsData = statisticsService.getSimpleStats(statement);
		if(statsData == null) return notFound("post");
		return ok(statsData);
	}

	@Override
	public Response authorStats(String end, String start, Integer authorId) throws IOException {
		StatStatement statement = new StatStatement("author", authorId, start, end);
		StatsData statsData = statisticsService.getSimpleStats(statement);
		if(statsData == null) return notFound("author");

		return ok(statsData);
	}

	@Override
	public Response networkStats(String end, String start) throws IOException {
		StatStatement statement = new StatStatement("tenant", TenantContextHolder.getCurrentTenantId(), start, end);
		StatsData networkData = statisticsService.getNetworkStats(statement);
		if(networkData == null) return notFound("network");

		return ok(networkData);
	}

	@Override
	public Response stationStats(String end, String start, Integer stationId) throws IOException {
		StatStatement statement = new StatStatement("station", stationId, start, end);
		StatsData stationData = statisticsService.getSimpleStats(statement);
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

	private void checkSize(Integer size) {
		if(size == null) size = 5;

		if(size < 1) badRequest("size", "must be bigger then 0 (zero)");
	}
}
