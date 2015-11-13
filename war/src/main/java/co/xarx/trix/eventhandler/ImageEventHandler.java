package co.xarx.trix.eventhandler;

import co.xarx.trix.auth.TrixAuthenticationProvider;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.FileContentsRepository;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.ImageService;
import co.xarx.trix.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@RepositoryEventHandler(Image.class)
@Component
public class ImageEventHandler {

	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	private FileContentsRepository fileContentsRepository;

	@Autowired
	private ImageService imageService;

	@HandleBeforeCreate
	public void handleBeforeCreate(Image image) throws Exception {
		if(image.pictures != null && !image.pictures.isEmpty()) return;

		if (image.type == null || image.type.trim().isEmpty())
			image.type = Image.Type.POST.toString();

		if (image.original == null) {
			throw new NullPointerException("original icon can't be null");
		}

		Image newImage;
		Network network = authProvider.getNetwork();
		FileContents originalFile = fileContentsRepository.findOne(image.original.id);
		//if is external, is already uploaded to amazon, so the file was uploaded before
		if (originalFile.type.equals(File.EXTERNAL) && amazonCloudService.exists(network.subdomain, originalFile.hash)) {
			Image existingImage = imageService.getImageFromHashAndType(image.type, originalFile.hash, network.id);

			java.io.File tempOriginalFile =
					FileUtil.downloadFile(FileUtil.createNewTempFile(), amazonCloudService.getPublicImageURL(network.subdomain, originalFile.hash));

			newImage = imageService.createNewImageFromExistingImage(network.subdomain, false, existingImage, tempOriginalFile);
		} else {
			//if it's running here, this is a new image and needs to upload all sizes
			try (InputStream inputStream = originalFile.contents.getBinaryStream()) {
				newImage = imageService.createNewImage(image.type, network.subdomain, inputStream, originalFile.mime, network.id, false, true);
				originalFile.contents = null;
				originalFile.type = File.EXTERNAL;
			} catch (Exception e) {
				throw new Exception("something bad happened uploading new image", e);
			}
		}

		for(Picture p : newImage.pictures) {
			p.image = image;
		}

		image.pictures = newImage.pictures;
		image.vertical = newImage.vertical;
		image.original = newImage.original;
		image.large = newImage.large;
		image.medium = newImage.medium;
		image.small = newImage.small;
	}
}