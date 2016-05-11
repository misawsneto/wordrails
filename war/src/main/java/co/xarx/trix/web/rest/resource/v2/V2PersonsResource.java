package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.UserPermissionData;
import co.xarx.trix.services.security.PersonPermissionService;
import co.xarx.trix.web.rest.api.v2.V2PersonsApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.stereotype.Component;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.core.Response;
import java.util.List;

@Component
@NoArgsConstructor
public class V2PersonsResource implements V2PersonsApi {

	private PersonPermissionService personPermissionService;

	@Autowired
	public V2PersonsResource(PersonPermissionService personPermissionService) {
		this.personPermissionService = personPermissionService;
	}

	@Override
	public Response searchPersons(String query, List<Integer> stations, @DefaultValue("0") Integer page, @DefaultValue("10") Integer size, List<String> orders, List<String> embeds) {
		return null;
	}

	@Override
	public Response getPermissions(String username, Integer stationId) {
		UserPermissionData data = personPermissionService.getPermissions(new PrincipalSid(username), stationId);

		return Response.ok().entity(data).build();
	}

	@Override
	public Response getPermissions(String username) {
		UserPermissionData data = personPermissionService.getPermissions(new PrincipalSid(username));

		return Response.ok().entity(data).build();
	}
}
