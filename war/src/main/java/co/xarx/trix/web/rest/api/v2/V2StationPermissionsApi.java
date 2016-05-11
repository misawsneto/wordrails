package co.xarx.trix.web.rest.api.v2;

import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/v2/stations/{stationId}/permissions")
@Produces(MediaType.APPLICATION_JSON)
public interface V2StationPermissionsApi {

	@GET
	@Path("/")
	@PreAuthorize("hasPermission(#sid, 'co.xarx.trix.domain.Station', 'read')")
	Response getPermissions(@PathParam("stationId") @P("sid") Integer stationId) throws IOException;
}
