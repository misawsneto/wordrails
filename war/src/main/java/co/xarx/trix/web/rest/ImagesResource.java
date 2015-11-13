package co.xarx.trix.web.rest;

import co.xarx.trix.domain.Image;
import co.xarx.trix.services.ImageService;
import co.xarx.trix.util.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/images")
@Consumes(MediaType.WILDCARD)
@Component
public class ImagesResource {

	private static final Integer MAX_SIZE = 6291456;

	@Autowired
	private ImageService imageService;

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadImage(@QueryParam("imageType") String type, @Context HttpServletRequest request) throws FileUploadException, IOException {
		FileItem item = FileUtil.getFileFromRequest(request);

		if (item == null) {
			return Response.noContent().build();
		} else if (!validate(item)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		String hash = FileUtil.getHash(item.getInputStream());
		Integer networkId = (Integer) request.getSession().getAttribute("networkId");
		String networkSubdomain = (String) request.getSession().getAttribute("networkSubdomain");

		Image newImage = imageService.createNewImage(type, networkSubdomain, item.getInputStream(), item.getContentType(), networkId, true, true);

		return Response.ok().entity("{\"hash\":\"" + hash + "\", \"imageId\":" + newImage.id + "}").build();
	}

	private boolean validate(FileItem item) throws FileUploadException {
		if (item.getFieldName().equals("contents") || item.getFieldName().equals("file")) {
			if (item.getSize() > MAX_SIZE) {
				throw new FileUploadException("Maximum file size is 6MB");
			}

			return true;
		}
		return false;
	}
}