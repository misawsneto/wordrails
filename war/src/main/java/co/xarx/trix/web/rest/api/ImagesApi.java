package co.xarx.trix.web.rest.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/images")
@Consumes(MediaType.WILDCARD)
public interface ImagesApi {

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	Response uploadImage(@QueryParam("imageType") String type) throws Exception;

	@GET
	@Path("/get/{hash}")
	Response getImage(@PathParam("hash") String hash, @QueryParam("size") String size) throws IOException;
}
