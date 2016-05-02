package co.xarx.trix.web.rest.api.v1;

import co.xarx.trix.api.v2.StatsData;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/stats")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StatsApi {

	@GET
	@Path("/me")
	@PreAuthorize("isAuthenticated()")
	StatsData personStats(@QueryParam("date") String date) throws IOException;

	@GET
	@Path("/post")
	@PreAuthorize("isAuthenticated()")
	StatsData postStats(@QueryParam("date") String date, @QueryParam("beggining") String beginning, @QueryParam("postId") Integer postId) throws IOException;

	@GET
	@Path("/author")
	@PreAuthorize("isAuthenticated()")
	StatsData authorStats(@QueryParam("date") String date, @QueryParam("beggining") String beginning, @QueryParam("authorId") Integer authorId) throws IOException;

	@GET
	@Path("/network")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response networkStats(@QueryParam("date") String date, @QueryParam("beggining") String beginning) throws IOException;

	@GET
	@Path("/network")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response stationStats(@QueryParam("date") String date, @QueryParam("beggining") String beginning, @QueryParam("stationId") Integer stationId) throws IOException;
}
