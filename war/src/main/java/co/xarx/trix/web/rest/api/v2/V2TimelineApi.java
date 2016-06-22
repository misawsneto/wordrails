package co.xarx.trix.web.rest.api.v2;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v2/persons/{username}")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface V2TimelineApi {

	@GET
	@Path("/timeline")
	Response getPersonTimeline(@PathParam("username") String username);
}
