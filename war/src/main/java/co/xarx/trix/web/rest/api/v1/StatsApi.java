package co.xarx.trix.web.rest.api.v1;

import co.xarx.trix.api.v2.StatsData;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Path("/stats")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StatsApi {

	@GET
	@Path("/post")
	@PreAuthorize("isAuthenticated()")
	StatsData postStats(@QueryParam("end") String end, @QueryParam("start") String start, @QueryParam("postId") Integer postId) throws IOException;

	@GET
	@Path("/author")
	@PreAuthorize("isAuthenticated()")
	StatsData authorStats(@QueryParam("end") String end, @QueryParam("start") String start, @QueryParam("authorId") Integer authorId) throws IOException;

	@GET
	@Path("/network")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	StatsData networkStats(@QueryParam("end") String end, @QueryParam("start") String start) throws IOException;

	@GET
	@Path("/station")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	StatsData stationStats(@QueryParam("end") String end, @QueryParam("start") String start, @QueryParam("stationId") Integer stationId) throws IOException;

	@GET
	@Path("/storage")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Map<String, Integer> getNetworkUsedSpace();

	@GET
	@Path("/countPostReads")
	Map<Integer, Integer> countReadsByPostIds(@QueryParam("postIds") List<Integer> postIds);
}
