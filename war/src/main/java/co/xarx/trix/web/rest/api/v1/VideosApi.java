package co.xarx.trix.web.rest.api.v1;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/videos")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface VideosApi {

	@GET
	@Path("/{id:\\d+}")
	void getVideo(@PathParam("id") int videoId) throws ServletException, IOException;

	@POST
	@Path("/")
	void postVideo() throws ServletException, IOException;
}