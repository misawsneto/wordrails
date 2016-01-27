package co.xarx.trix.web.rest;

import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.QImage;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.ImageService;
import co.xarx.trix.util.FileUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Path("/images")
@Consumes(MediaType.WILDCARD)
@Component
public class ImagesResource {

	private static final Integer MAX_SIZE = 6291456;

	@Autowired
	private ImageService imageService;
	@Autowired
	private ImageRepository imageRepository;

	@Context
	private HttpServletRequest request;
	@Context
	private UriInfo uriInfo;

	@Autowired private AmazonCloudService amazonCloudService;

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


		Image newImage = imageService.createNewImage(type, item.getInputStream(), item.getContentType(), true, true);

		return Response.ok().entity("{\"hash\":\"" + hash +
				"\", \"imageId\":" + newImage.id +
				"\", \"imageHash\":" + newImage.hashs.get(Image.SIZE_ORIGINAL) +
				", \"link\": \"" + amazonCloudService.getPublicImageURL(hash) +
				"\", \"filelink\": \"" + amazonCloudService.getPublicImageURL(hash) + "\"}").build();
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

	@GET
	@Path("/get")
	public Response getImage(@QueryParam("hash") String hash, @QueryParam("size") String size, @Context HttpServletResponse response) throws IOException {
		Image image = imageRepository.findOne(QImage.image.hashs.contains("original", hash));
		hash = amazonCloudService.getPublicImageURL(image.hashs.get(size));

		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=2592000");

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 30);
		String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format(c.getTime());
		response.setHeader("Expires", o);

		if (hash != null && !hash.isEmpty()) {
			response.sendRedirect(amazonCloudService.getPublicImageURL(hash));
			return Response.ok().build();
		}

		return Response.status(Response.Status.NO_CONTENT).build();
	}
}