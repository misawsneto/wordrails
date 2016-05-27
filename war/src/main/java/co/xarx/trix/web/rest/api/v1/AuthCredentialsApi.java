package co.xarx.trix.web.rest.api.v1;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/authCredentials")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface AuthCredentialsApi {
	@GET
	@Path("/{id:\\d+}")
	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Post', 'read') or hasRole('ADMIN')")
	void getAuthCredentials(@PathParam("id") @P("id") int postId) throws ServletException, IOException;

	@PUT
	@Path("/{id:\\d+}")
	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Post', 'write') or hasRole('ADMIN')")
	void putAuthCredentials(@PathParam("id") Integer id) throws ServletException, IOException;

	@POST
	@Path("/")
	void postAuthCredentials() throws ServletException, IOException;
}
