package co.xarx.trix.web.rest.api.v2;

import co.xarx.trix.api.ContentResponse;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/v2/statistics")
@Produces(MediaType.APPLICATION_JSON)
public interface V2StatisticsApi {

	@GET
	@Path("/getMostPopular")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse getMostCommonTerm(@QueryParam("page") Integer page,
											 @QueryParam("size") Integer size,
											 @QueryParam("startTime") Long startTime,
											 @QueryParam("endTime") Long endTime,
											 @QueryParam("field") String field);

	@GET
	@Path("/popularNetworks")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse getPopularNetworks(@QueryParam("page") Integer page, @QueryParam("size") Integer size);
}
