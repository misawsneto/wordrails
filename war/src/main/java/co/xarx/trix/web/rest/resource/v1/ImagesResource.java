package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.api.ImageUploadResponse;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.FileService;
import co.xarx.trix.services.ImageService;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.ImagesApi;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
public class ImagesResource extends AbstractResource implements ImagesApi {

	private ImageService imageService;
	private AmazonCloudService amazonCloudService;
	private FileService fileService;
	private PersonRepository personRepository;
	private PostRepository postRepository;

	@Autowired
	@Qualifier("simpleMapper")
	ObjectMapper simpleMapper;

	@Autowired
	public ImagesResource(ImageService imageService, AmazonCloudService amazonCloudService, FileService fileService, PersonRepository personRepository
			, PostRepository postRepository) {
		this.imageService = imageService;
		this.amazonCloudService = amazonCloudService;
		this.fileService = fileService;
		this.personRepository = personRepository;
		this.postRepository = postRepository;
	}

	public static class ImageUpload {
		public Integer id;
		public String hash;
		public Integer imageId;
		public String link;
		public String fileLink;
		public String credits;
	}

	@Override
	public Response uploadImage(@QueryParam("imageType") String type) throws Exception {
		FileItem item = getFileFromRequest();

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
		imageUpload.credits = newImage.getCredits();

		String hash = imageUpload.hash;
		ImageUploadResponse iur = new ImageUploadResponse();
		iur.hash = hash;
		iur.imageId = newImage.getId();
		iur.id = newImage.getId();
		iur.imageHash = newImage.getHashes().get(Image.SIZE_ORIGINAL);
		iur.link = amazonCloudService.getPublicImageURL(hash);
		iur.filelink = amazonCloudService.getPublicImageURL(hash);
		iur.credits = imageUpload.credits;

		return Response.ok().entity(simpleMapper.writeValueAsString(iur)).build();
	}

	@Override
	public Response getImage(String hash, String size) throws IOException {
		response.setHeader("Pragma", "public");
		response.setHeader("Cache-Control", "max-age=2592000");

		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.DATE, 30);
		String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format(c.getTime());
		response.setHeader("Expires", o);

		Map<String, String> hashes;
		if ("audio".equals(hash)) {
			response.sendRedirect("http://" + request.getHeader("Host") +
					"/images/" +
					"audio.png");

		}else if ("video".equals(hash)) {
			response.sendRedirect("http://" + request.getHeader("Host") +
					"/images/" +
					"video.png");

		}else {
			try {
				hashes = imageService.getHashes(hash);
			} catch (EntityNotFoundException e) {
				throw new NotFoundException("Image does not exist. Hash:" + hash);
			}
			hash = hashes.get(size);
			if (StringUtils.isEmpty(hash))
				return Response.status(Response.Status.NO_CONTENT).build();

			response.sendRedirect(amazonCloudService.getPublicImageURL(hash));
		}

		return Response.ok().build();
	}

	@Override
	public void findImagesOrderByDate() throws IOException {
		forward();
	}

	@Override
	public Response getPersonImage(Integer id, String size, String type) throws IOException {

		Person person = personRepository.findOne(id);

		String hash = null;

		if(person.getImage() != null && person.getImage().getExternalImageUrl() != null){
			response.sendRedirect(person.getImage().getExternalImageUrl());
			return Response.ok().build();
		}

		if(person != null && type != null && type.toUpperCase().equals(Image.Type.PROFILE_PICTURE.toString()) && person
				.getImage() != null)
			hash = person.getImage().getHashes().get(size);
		else if(person != null && type != null && type.toUpperCase().equals(Image.Type.COVER.toString()) &&
				person.getCover() != null)
			hash = person.getCover().getHashes().get(size);
		else
			return Response.status(404).build();

		if(StringUtils.isEmpty(hash))
			return Response.status(Response.Status.NO_CONTENT).build();

//		response.setHeader("Pragma", "public");
//		response.setHeader("Cache-Control", "max-age=2592000");
//
//		Calendar c = Calendar.getInstance();
//		c.setTime(new Date());
//		c.add(Calendar.DATE, 30);
//		String o = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss zzz").format(c.getTime());
//		response.setHeader("Expires", o);

		response.sendRedirect(amazonCloudService.getPublicImageURL(hash));
		return Response.ok().build();
	}

	@Override
	public Response getPostImage(Integer id, String size) throws IOException {
		Post post = postRepository.findOne(id);

		String hash = null;

		if(post != null &&  post.getFeaturedImage() != null)
			hash = post.getFeaturedImage().getHashes().get(size);
		else
			return Response.status(404).build();

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


	public Response updateImageCredits(ImageUpload imageUpload){
		if(imageUpload != null && imageUpload.id != null && imageUpload.credits != null){
			imageService.updateImageCredits(imageUpload);
		}
		return Response.ok().build();
	}
}