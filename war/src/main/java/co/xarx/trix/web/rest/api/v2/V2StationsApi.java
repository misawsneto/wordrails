package co.xarx.trix.web.rest.api.v2;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/v2/stations")
@Produces(MediaType.APPLICATION_JSON)
public interface V2StationsApi {

	@GET
	@Path("/")
	Response getStations() throws IOException;
}
