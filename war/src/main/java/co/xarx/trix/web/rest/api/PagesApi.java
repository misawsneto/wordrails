package co.xarx.trix.web.rest.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/stations")
public interface PagesApi {

	@GET
	@Path("/{stationId}/pages")
	@Produces(MediaType.APPLICATION_JSON)
	Response getPages(@PathParam("stationId") Integer stationId) throws IOException;
}
