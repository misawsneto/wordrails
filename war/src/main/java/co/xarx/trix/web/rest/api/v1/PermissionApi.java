package co.xarx.trix.web.rest.api.v1;

import co.xarx.trix.api.StationRolesUpdate;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/permission")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PermissionApi {

	@PUT
	@Path("/stations/update")
	@Transactional
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response updateStationsPermissions(StationRolesUpdate dto);

	@POST
	@Path("/stations/clean")
	@Transactional
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	Response cleanStationsPermissions(StationRolesUpdate dto);
}
