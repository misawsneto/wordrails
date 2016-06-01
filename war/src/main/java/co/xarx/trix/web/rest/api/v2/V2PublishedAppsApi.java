package co.xarx.trix.web.rest.api.v2;

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
public interface V2PublishedAppsApi {
	@PUT
	@Path("/")
	void updatePublishedApp(AuthCredentialDto authCredential) throws ServletException, IOException;

	@GET
	@Path("/")
	AuthCredential getPublishedApp() throws ServletException, IOException;

}
