package co.xarx.trix.web.rest.api.v1;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/stationPerspectives")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StationPerspectiveApi {

	@POST
	@Path("/")
	void postStationPerspective() throws IOException;

	@PUT
	@Path("/{id}")
	void putStationPerspective(@PathParam("id") Integer stationPerspectiveId) throws IOException;

}
