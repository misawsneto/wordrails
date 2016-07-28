package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.StationPermissionData;
import co.xarx.trix.services.security.StationPermissionService;
import co.xarx.trix.web.rest.api.v2.V2StationPermissionsApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;

@Component
@NoArgsConstructor
public class V2StationPermissionsResource implements V2StationPermissionsApi {

	private StationPermissionService stationPermissionService;

	@Autowired
	public V2StationPermissionsResource(StationPermissionService stationPermissionService) {
		this.stationPermissionService = stationPermissionService;
	}

	@Override
	public Response getPermissions(@P("sid") Integer stationId) throws IOException {
		StationPermissionData data = stationPermissionService.getPermissions(stationId);
		stationPermissionService.getPersons(data);

		return Response.ok().entity(data).build();
	}
}
