package co.xarx.trix.web.rest.api.v2;

import co.xarx.trix.domain.NetworkCreate;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/networkCreates")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface V2NetworkCreateApi {

	@POST
	@Path("/")
	Response postNetworkCreate(NetworkCreate networkCreate) throws ServletException, IOException;
}