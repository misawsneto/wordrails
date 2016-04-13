package co.xarx.trix.web.rest.resource;

import co.xarx.trix.api.ImageUploadResponse;
import co.xarx.trix.domain.Image;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.FileService;
import co.xarx.trix.services.ImageService;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.ImagesApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

@Component
public class ImagesResource extends AbstractResource implements ImagesApi {

	private ImageService imageService;
	private AmazonCloudService amazonCloudService;
	private FileService fileService;

	@Autowired
	@Qualifier("simpleMapper")
	ObjectMapper simpleMapper;

	@Autowired
	public ImagesResource(ImageService imageService, AmazonCloudService amazonCloudService, FileService fileService) {
		this.imageService = imageService;
		this.amazonCloudService = amazonCloudService;
		this.fileService = fileService;
	}

	private static class ImageUpload {
		String hash;
		Integer imageId;
		String link;
		String fileLink;
	}

	@Override
	public Response uploadImage(@QueryParam("imageType") String type) throws Exception {
		FileItem item = FileUtil.getFileFromRequest(request);

		if (item == null) {
			return Response.noContent().build();
		} else if (!fileService.validate(item, FileService.MAX_SIZE_8)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}


		Image newImage = new Image(type);
		newImage.setTitle(item.getName());
		File originalFile = FileUtil.createNewTempFile(item.getInputStream());

		newImage = imageService.createAndSaveNewImage(type, item.getName(), originalFile, item.getContentType());

		if (originalFile.exists())
			originalFile.delete();

		ImageUpload imageUpload = new ImageUpload();
		imageUpload.hash = FileUtil.getHash(item.getInputStream());
		imageUpload.imageId = newImage.getId();
		imageUpload.link = amazonCloudService.getPublicImageURL(imageUpload.hash);
		imageUpload.fileLink = imageUpload.link;

		String hash = imageUpload.hash;
		ImageUploadResponse iur = new ImageUploadResponse();
		iur.hash = hash;
		iur.imageId = newImage.getId();
		iur.id = newImage.getId();
		iur.imageHash = newImage.getHashs().get(Image.SIZE_ORIGINAL);
		iur.link = amazonCloudService.getPublicImageURL(hash);
		iur.filelink = amazonCloudService.getPublicImageURL(hash);

		return Response.ok().entity(simpleMapper.writeValueAsString(iur)).build();
	}

	@Override
	public Response getImage(String hash, String size) throws IOException {

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