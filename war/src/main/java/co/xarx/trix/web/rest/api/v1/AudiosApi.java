package co.xarx.trix.web.rest.api.v1;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/audios")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface AudiosApi {

	@GET
	@Path("/{id:\\d+}")
	void getAudio(@PathParam("id") int audioId) throws ServletException, IOException;

	@GET
	@Path("/search/findAudiosOrderByDate")
	void findAudiosOrderByDate() throws ServletException, IOException;

	@POST
	@Path("/")
	void postAudio() throws ServletException, IOException;
}