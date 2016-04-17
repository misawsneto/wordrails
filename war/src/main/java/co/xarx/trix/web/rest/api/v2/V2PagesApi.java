package co.xarx.trix.web.rest.api.v2;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/v2/stations")
public interface V2PagesApi {

	@GET
	@Path("/{stationId}/pages")
	@Produces(MediaType.APPLICATION_JSON)
	Response getPages(@PathParam("stationId") Integer stationId) throws IOException;

	@GET
	@Path("/{stationId}/pages/{pageId}/sections")
	@Produces(MediaType.APPLICATION_JSON)
	Response getSections(@PathParam("stationId") Integer stationId,
						 @PathParam("pageId") Integer pageId,
						 @QueryParam("size") @DefaultValue("10") Integer size,
						 @QueryParam("page") @DefaultValue("0") Integer page) throws IOException;
}
