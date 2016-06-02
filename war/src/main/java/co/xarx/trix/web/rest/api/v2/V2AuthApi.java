package co.xarx.trix.web.rest.api.v2;

import co.xarx.trix.api.v2.AuthCredentialData;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/v2/auth")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface V2AuthApi {

	@PUT
	@Path("/social/credentials")
	Response updateAuthCredentials(AuthCredentialData data) throws ServletException, IOException;

	@GET
	@Path("/social/credentials")
	Response getAuthCredentials() throws ServletException, IOException;

}
