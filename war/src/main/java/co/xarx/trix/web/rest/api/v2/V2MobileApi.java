package co.xarx.trix.web.rest.api.v2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/v2/mobile")
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface V2MobileApi {

	@POST
	@Path("/apple/certificate")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	Response updateAppleCertificate() throws ServletException, IOException;

	@POST
	@Path("/apple/password")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	Response updateApplePassword(@FormParam("password") String password) throws ServletException, IOException;

}