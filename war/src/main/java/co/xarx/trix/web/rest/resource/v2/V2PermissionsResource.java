package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.StationPermissionData;
import co.xarx.trix.services.security.StationPermissionService;
import co.xarx.trix.web.rest.api.v2.V2PermissionsApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;

@Component
@NoArgsConstructor
public class V2PermissionsResource implements V2PermissionsApi {

	private StationPermissionService stationPermissionService;

	@Autowired
	public V2PermissionsResource(StationPermissionService stationPermissionService) {
		this.stationPermissionService = stationPermissionService;
	}

	@Override
	public Response getPermissions(@P("sid") Integer stationId) throws IOException {
		StationPermissionData data = stationPermissionService.getPermissions(stationId);

		return Response.ok().entity(data).build();
	}
}
