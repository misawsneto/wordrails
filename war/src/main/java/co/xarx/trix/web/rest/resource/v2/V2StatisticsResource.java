package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.services.analytics.StatisticsService;
import co.xarx.trix.web.rest.api.v2.V2StatisticsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Component
public class V2StatisticsResource implements V2StatisticsApi{

	@Autowired
	public StatisticsService statisticsService;

	@GET
	@Path("/getMostPopular")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse getMostCommonTerm(Integer page,
											 Integer size,
											 Long startTime,
											 Long endTime,
											 String field){
		ContentResponse response = new ContentResponse();
		response.content = statisticsService.findMostPopular(field, startTime, endTime, size);
		return response;
	}

	@GET
	@Path("/popularNetworks")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse getPopularNetworks(Integer page, Integer size){
		ContentResponse response = new ContentResponse();
		response.content = statisticsService.getPorpularNetworks();
		return response;
	}
}
