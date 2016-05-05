package co.xarx.trix.web.rest.api.v1;

import javax.servlet.ServletException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/videos")
@Consumes(MediaType.WILDCARD)
public interface VideosApi {

	@GET
	@Path("/{id:\\d+}")
	void getVideo(@PathParam("id") int videoId) throws ServletException, IOException;
}