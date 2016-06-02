package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.AuthCredentialData;
import co.xarx.trix.services.security.AuthCredentialsService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2AuthApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Component
@NoArgsConstructor
public class V2AuthResource extends AbstractResource implements V2AuthApi {

	@Autowired
	private AuthCredentialsService credentialsService;

	@Override
	public Response updateAuthCredentials(AuthCredentialData data) throws ServletException, IOException {
		credentialsService.updateCredentials(data);

		return Response.ok().build();
	}

	@Override
	public Response getAuthCredentials() throws ServletException, IOException {
		AuthCredentialData credentials = credentialsService.getCredentials();

		if(credentials == null)
			return Response.noContent().build();

		return Response.ok().entity(credentials).build();
	}
}