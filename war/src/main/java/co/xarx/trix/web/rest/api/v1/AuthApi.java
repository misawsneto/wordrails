package co.xarx.trix.web.rest.api.v1;

import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/auth")
public interface AuthApi {

	@POST
	@Path("/signin")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@PreAuthorize("isAnonymous()")
	Response signin(@FormParam("provider") String providerId, @FormParam("userId") String userId, @FormParam("accessToken") String accessToken) throws IOException;

	@POST
	@Path("/forgotPassword")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@PreAuthorize("isAnonymous()")
	Response resetPassword(@FormParam("email") String email);

	@PUT
	@Path("/{hash}")
	@PreAuthorize("isAnonymous()")
	Response updatePassword(@PathParam("hash") String hash, @FormParam("password") String password);
}
