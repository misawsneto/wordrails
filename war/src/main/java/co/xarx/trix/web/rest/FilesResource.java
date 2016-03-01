package co.xarx.trix.web.rest;

import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.services.AmazonCloudService;
import org.apache.commons.fileupload.FileUploadException;
import org.jboss.resteasy.annotations.cache.Cache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Path("/files")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
public class FilesResource {

	private static final Integer MAX_SIZE = 6291456;

	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private AmazonCloudService amazonCloudService;


	@Deprecated
	@PUT
	@Path("{id}/contents")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	public Response putFileContents(@PathParam("id") Integer id, @Context HttpServletRequest request) throws FileUploadException, IOException {
		return Response.status(Status.GONE).build();
	}

	@POST
	@Path("/contents/simple")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response postSimpleFileContents(@Context HttpServletRequest request) throws FileUploadException, IOException {
		return Response.status(Status.GONE).build();
	}

	@GET
	@Path("{id}/contents")
	@Cache(isPrivate = false, maxAge = 31536000)
	public Response getFileContents(@PathParam("id") Integer id, @Context HttpServletResponse response) throws SQLException, IOException {
		String hash = fileRepository.findExternalHashById(id);

		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=2592000");

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 30);
		//HTTP header date format: Thu, 01 Dec 1994 16:00:00 GMT
		String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format(c.getTime());
		response.setHeader("Expires", o);

		if (hash != null && !hash.isEmpty()) {
			response.sendRedirect(amazonCloudService.getPublicImageURL(hash));
			return Response.ok().build();
		}

		return Response.status(Status.NO_CONTENT).build();
	}


}