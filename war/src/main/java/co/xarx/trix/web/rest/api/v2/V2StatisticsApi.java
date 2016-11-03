package co.xarx.trix.web.rest.api.v2;

import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/v2/statistics")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface V2StatisticsApi {

	@GET
	@Path("/getMostPopular")
	@Produces(MediaType.APPLICATION_JSON)
	Response getMostPopular(@QueryParam("page") @DefaultValue("0") Integer page,
							@QueryParam("size") @DefaultValue("20") Integer size,
							@QueryParam("start") String startTime,
							@QueryParam("end") String endTime,
							@QueryParam("field") String field,
							@QueryParam("byFields") List<String> byFields,
							@QueryParam("byValues") List<String> byValues);

	@GET
	@Path("/popularNetworks")
	@PreAuthorize("isAuthenticated()")
	Response getPopularNetworks(@QueryParam("page") @DefaultValue("0") Integer page,
								@QueryParam("size") @DefaultValue("20") Integer size,
								@QueryParam("start") String start,
								@QueryParam("end") String end);

	@GET
	@Path("/post")
	@PreAuthorize("isAuthenticated()")
	Response postStats(@QueryParam("end") String end, @QueryParam("start") String start, @QueryParam("postId") Integer postId) throws IOException;

	@GET
	@Path("/author")
	@PreAuthorize("isAuthenticated()")
	Response authorStats(@QueryParam("end") String end, @QueryParam("start") String start, @QueryParam("authorId") Integer authorId) throws IOException;

	@GET
	@Path("/network")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response networkStats(@QueryParam("end") String end, @QueryParam("start") String start) throws IOException;

	@GET
	@Path("/station")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response stationStats(@QueryParam("end") String end, @QueryParam("start") String start, @QueryParam("stationId") Integer stationId) throws IOException;

	@GET
	@Path("/storage")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response getNetworkUsedSpace();

	@GET
	@Path("/getPostReads")
	@PreAuthorize("isAuthenticated()")
	Response countReadsByPostIds(@QueryParam("postIds") List<Integer> postIds);

	@GET
	@Path("/countReadersByStation")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response countReadersByStation(@QueryParam("stationId") Integer stationId);

	@GET
	@Path("/dashboardStats")
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response dashboardStats();
}
