package co.xarx.trix.web.rest;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.services.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("/stats")
@Consumes(MediaType.WILDCARD)
@Component
public class StatisticsResource {

	@Autowired
	public StatisticsService statisticsService;

	@GET
	@Path("/getMostPopular")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse getMostCommonTerm(@QueryParam("page") Integer page,
											 @QueryParam("size") Integer size,
											 @QueryParam("startTime") Long startTime,
											 @QueryParam("endTime") Long endTime,
											 @QueryParam("field") String field){
		ContentResponse response = new ContentResponse();
		response.content = statisticsService.findMostPopular(field, startTime, endTime, size);
		return response;
	}

	@GET
	@Path("/popularNetworks")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse getPopularNetworks(@QueryParam("page") Integer page, @QueryParam("size") Integer size){
		ContentResponse response = new ContentResponse();
		response.content = statisticsService.getPorpularNetworks();
		return response;
	}
}
