package co.xarx.trix.web.rest;

import co.xarx.trix.api.StationRolesUpdate;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.services.auth.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/permission")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class PermissionResource {

	@Autowired
	private PermissionService permissionService;
	@Autowired
	private AuthService authService;

	@PUT
	@Path("/stations/update")
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Response updateStationsPermissions(StationRolesUpdate dto) {
		Assert.notNull(dto, "Dto must not be null");
		Assert.notEmpty(dto.usernames, "Person ids must have elements");
		Assert.notEmpty(dto.stationsIds, "Station ids must have elements");

		try {
			dto.usernames.remove(authService.getLoggedUsername());
		} catch (AuthenticationCredentialsNotFoundException e) {
			throw new UnauthorizedException("Permission denied");
		}

		permissionService.updateStationsPermissions(dto.usernames, dto.stationsIds, dto.writer, dto.editor, dto.admin);

		return Response.status(Response.Status.OK).build();
	}

	@POST
	@Path("/stations/clean")
	@Transactional
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public Response cleanStationsPermissions(StationRolesUpdate dto) {
		Assert.notNull(dto, "Dto must not be null");
		Assert.notEmpty(dto.usernames, "Person ids must have elements");
		Assert.notEmpty(dto.stationsIds, "Station ids must have elements");

		dto.usernames.remove(authService.getLoggedUsername());

		permissionService.deleteStationPermissions(dto.usernames, dto.stationsIds);

		return Response.status(Response.Status.OK).build();
	}
}
