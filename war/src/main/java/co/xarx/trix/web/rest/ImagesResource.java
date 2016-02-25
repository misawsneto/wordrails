package co.xarx.trix.web.rest;

import co.xarx.trix.api.ImageUploadResponse;
import co.xarx.trix.domain.Image;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.ImageService;
import co.xarx.trix.util.FileUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Path("/images")
@Consumes(MediaType.WILDCARD)
@Component
public class ImagesResource {

	private static final Integer MAX_SIZE = 6291456;

	@Autowired
	private ImageService imageService;

	@Context
	private HttpServletRequest request;
	@Autowired
	private AmazonCloudService amazonCloudService;

	public static class ImageUpload {
		public String hash;
		public Integer imageId;
		public String link;
		public String fileLink;
	}


	@Autowired
	@Qualifier("simpleMapper")
	ObjectMapper simpleMapper;

	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadImage(@QueryParam("imageType") String type, @Context HttpServletRequest request) throws Exception {
		FileItem item = FileUtil.getFileFromRequest(request);

		if (item == null) {
			return Response.noContent().build();
		} else if (!validate(item)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}


		Image newImage = new Image(type);
		newImage.title = item.getName();
		File originalFile = FileUtil.createNewTempFile(item.getInputStream());

		newImage = imageService.createAndSaveNewImage(type, item.getName(), originalFile, item.getContentType());

		if (originalFile.exists())
			originalFile.delete();

		ImageUpload imageUpload = new ImageUpload();
		imageUpload.hash = FileUtil.getHash(item.getInputStream());
		imageUpload.imageId = newImage.id;
		imageUpload.link = amazonCloudService.getPublicImageURL(imageUpload.hash);
		imageUpload.fileLink = imageUpload.link;

		String hash = imageUpload.hash;
		ImageUploadResponse iur = new ImageUploadResponse();
		iur.hash = hash;
		iur.imageId = newImage.id;
		iur.id = newImage.id;
		iur.imageHash = newImage.hashs.get(Image.SIZE_ORIGINAL);
		iur.link = amazonCloudService.getPublicImageURL(hash);
		iur.filelink = amazonCloudService.getPublicImageURL(hash);

		return Response.ok().entity(simpleMapper.writeValueAsString(iur)).build();
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
	@Path("/get/{hash}")
	public Response getImage(@PathParam("hash") String hash, @QueryParam("size") String size, @Context HttpServletResponse response) throws IOException {

		Map<String, String> hashes;
		try {
			hashes = imageService.getHashes(hash);
		} catch (EntityNotFoundException e) {
			throw new NotFoundException("Image does not exist. Hash:" + hash);
		}

		hash = hashes.get(size);

		if(StringUtils.isEmpty(hash))
			return Response.status(Response.Status.NO_CONTENT).build();

		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=2592000");

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 30);
		String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format(c.getTime());
		response.setHeader("Expires", o);

		response.sendRedirect(amazonCloudService.getPublicImageURL(hash));
		return Response.ok().build();
	}
}