package co.xarx.trix.web.rest.api.v1;

import co.xarx.trix.web.rest.resource.v1.ImagesResource;

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

	@GET
	@Path("/search/findImagesOrderByDate")
	void findImagesOrderByDate() throws IOException;

	@GET
	@Path("/persons/{id}")
	Response getPersonImage(@PathParam("id") Integer id, @QueryParam("size") String size, @QueryParam("type") String
			type)
			throws IOException;

	@GET
	@Path("/posts/{id}")
	Response getPostImage(@PathParam("id") Integer id, @QueryParam("size") String size)
			throws IOException;

	@PUT
	@Path("/updateCredits")
	Response updateImageCredits(ImagesResource.ImageUpload image);
}
