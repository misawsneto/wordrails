package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.api.StationRolesUpdate;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.services.security.StationPermissionService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.PermissionApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

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

		List<Sid> sids = dto.usernames.stream().map(PrincipalSid::new).collect(Collectors.toList());

		stationPermissionService.updateStationsPermissions(sids, dto.stationsIds, dto.writer, dto.editor, dto.admin);

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
