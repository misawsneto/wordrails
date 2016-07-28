package co.xarx.trix.web.rest.api.v1;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/documents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface DocumentsApi {

	@GET
	@Path("/{id:\\d+}")
	void getDocument(@PathParam("id") int documentId) throws ServletException, IOException;

	@GET
	@Path("/search/findDocumentsOrderByDate")
	void findDocumentsOrderByDate() throws ServletException, IOException;

	@POST
	@Path("/")
	void postDocument() throws ServletException, IOException;
}