package co.xarx.trix.web.rest.api.v1;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/videos")
@Consumes(MediaType.WILDCARD)
public interface VideosApi {

	@GET
	@Path("/{id:\\d+}")
	void getVideo(@PathParam("id") int videoId) throws ServletException, IOException;

	@POST
	@Path("/")
	void postVideo() throws ServletException, IOException;
}