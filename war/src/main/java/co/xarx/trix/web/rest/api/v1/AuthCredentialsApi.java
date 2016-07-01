package co.xarx.trix.web.rest.api.v1;

import javax.servlet.ServletException;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.io.IOException;

@Path("/authCredentials")
public interface AuthCredentialsApi {

	@GET
	@Path("/search/findAuthCredentialByTenantId")
	public void findAuthCredentialByTenantId() throws IOException;

	@PUT
	@Path("/{id:\\d+}")
	void putAuthCredentials(@PathParam("id") Integer id) throws ServletException, IOException;
}