package co.xarx.trix.web.rest.api;

import org.jboss.resteasy.annotations.cache.Cache;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.sql.SQLException;

@Path("/files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FilesApi {

	@GET
	@Path("{id}/contents")
	@Cache(isPrivate = false, maxAge = 31536000)
	Response getFileContents(@PathParam("id") Integer id) throws SQLException, IOException;

	@POST
	@Path("/upload/video")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	Response uploadVideo() throws Exception;

	@POST
	@Path("/upload/audio")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	Response uploadAudio() throws Exception;

	@POST
	@Path("/upload/doc")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	Response uploadDoc() throws Exception;

	@GET
	@Path("/get/{hash}")
	Response getFile(@PathParam("hash") String hash) throws IOException;
}
