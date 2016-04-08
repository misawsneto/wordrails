package co.xarx.trix.web.rest.resource;

import co.xarx.trix.api.StationRolesUpdate;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.services.auth.StationPermissionService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.PermissionApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.ws.rs.core.Response;

@Component
@NoArgsConstructor
public class PermissionResource extends AbstractResource implements PermissionApi {

	@Autowired
	private StationPermissionService stationPermissionService;
	@Autowired
	private AuthService authService;

	@Override
	@Transactional
	public Response updateStationsPermissions(StationRolesUpdate dto) {
		Assert.notNull(dto, "Dto must not be null");
		Assert.notEmpty(dto.usernames, "Person ids must have elements");
		Assert.notEmpty(dto.stationsIds, "Station ids must have elements");

		try {
			dto.usernames.remove(authService.getLoggedUsername());
		} catch (AuthenticationCredentialsNotFoundException e) {
			throw new UnauthorizedException("Permission denied");
		}

		stationPermissionService.updateStationsPermissions(dto.usernames, dto.stationsIds, dto.writer, dto.editor, dto.admin);

		return Response.status(Response.Status.OK).build();
	}

	@Override
	@Transactional
	public Response cleanStationsPermissions(StationRolesUpdate dto) {
		Assert.notNull(dto, "Dto must not be null");
		Assert.notEmpty(dto.usernames, "Person ids must have elements");
		Assert.notEmpty(dto.stationsIds, "Station ids must have elements");

		dto.usernames.remove(authService.getLoggedUsername());

		stationPermissionService.deleteStationPermissions(dto.usernames, dto.stationsIds);

		return Response.status(Response.Status.OK).build();
	}
}
