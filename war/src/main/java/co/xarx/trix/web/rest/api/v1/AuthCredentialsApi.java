package co.xarx.trix.web.rest.api.v1;

import co.xarx.trix.api.AuthCredentialDto;
import co.xarx.trix.domain.AuthCredential;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/authCredentials")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface AuthCredentialsApi {
	@PUT
	@Path("/")
	public void updateAuthCredentials(AuthCredentialDto authCredential) throws ServletException, IOException;

	@GET
	@Path("/")
	public AuthCredential getAuthCredentials() throws ServletException, IOException;

}
