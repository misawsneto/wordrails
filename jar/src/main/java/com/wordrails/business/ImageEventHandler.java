package com.wordrails.business;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.persistence.FileContentsRepository;
import com.wordrails.persistence.FileRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.services.FileService;
import com.wordrails.util.WordrailsUtil;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

@RepositoryEventHandler(Image.class)
@Component
public class ImageEventHandler {

	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private FileService fileService;
	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private FileContentsRepository fileContentsRepository;

	@HandleBeforeCreate
	public void handleBeforeCreate(Image image) throws IOException, SQLException, FileUploadException {
		if (image.type == null || image.type.trim().isEmpty())
			image.type = Image.Type.POST.toString();

		if (!Image.containsType(image.type))
			throw new BadRequestException("Invalid Image Type:" + image.type);

		if (image.original == null) {
			return;
		}

		FileContents originalFile = fileContentsRepository.findOne(image.original.id);
		if (originalFile.type.equals(File.EXTERNAL)) { //if is external, is already uploaded to amazon, so the file was uploaded before
			Image existingImage = imageRepository.findByFileId(originalFile.id);
			image.small = existingImage.small;
			image.medium = existingImage.medium;
			image.large = existingImage.large;

			image.smallHash = existingImage.smallHash;
			image.mediumHash = existingImage.mediumHash;
			image.largeHash = existingImage.largeHash;
			image.originalHash = existingImage.originalHash;

			image.vertical = existingImage.vertical;
			return;
		}

		//if it's running here, this is a new image and needs to upload all sizes
		try (InputStream input = originalFile.contents.getBinaryStream()) {
			Network network = authProvider.getNetwork();

			byte[] bytes = WordrailsUtil.getBytes(input);
			BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
			String mime = originalFile.mime;
			String extension = mime.split("/").length == 2 ? mime.split("/")[1] : "jpeg";

			int maxSize = Math.max(bufferedImage.getHeight(), bufferedImage.getWidth());

			String originalHash = amazonCloudService.uploadPublicImage(new ByteArrayInputStream(bytes), originalFile.size, network.subdomain, "original", extension);

			originalFile.contents = null;
			originalFile.type = File.EXTERNAL;
			originalFile.hash = originalHash;
			if (originalFile.networkId == null || originalFile.networkId == 0) {
				originalFile.networkId = network.id;
			}
			fileContentsRepository.save(originalFile);

			FileInputStream smallResizedStream = fileService.resizeImage(bufferedImage, 150, extension);
			long smallSize = smallResizedStream.getChannel().size();
			String smallHash = amazonCloudService.uploadPublicImage(smallResizedStream, smallSize, network.subdomain, "small", extension);
			File small = new File();
			small.mime = mime;
			small.type = File.EXTERNAL;
			small.hash = smallHash;
			small.size = smallSize;
			small.networkId = network.id;
			fileRepository.save(small);

			File medium;
			String mediumHash;
			if (maxSize < 250) {
				mediumHash = originalHash;
				medium = image.original;
			} else {
				FileInputStream mediumResizedStream = fileService.resizeImage(bufferedImage, 300, extension);
				long mediumSize = mediumResizedStream.getChannel().size();
				mediumHash = amazonCloudService.uploadPublicImage(mediumResizedStream, mediumSize, network.subdomain, "medium", extension);
				medium = new File();
				medium.mime = mime;
				medium.type = File.EXTERNAL;
				medium.hash = mediumHash;
				medium.size = mediumSize;
				medium.networkId = network.id;
				fileRepository.save(medium);
			}

			File large;
			String largeHash;
			if (maxSize < 800) {
				largeHash = originalHash;
				large = image.original;
			} else {
				int largeSize = 1024;
				if (maxSize >= 1200 && maxSize < 1500) {
					largeSize = 1400;
				} else if (maxSize >= 1500) {
					largeSize = 1600;
				}

				FileInputStream largeResizedStream = fileService.resizeImage(bufferedImage, largeSize, extension);
				long largeLenght = largeResizedStream.getChannel().size();
				large = new File();
				large.mime = mime;
				largeHash = amazonCloudService.uploadPublicImage(largeResizedStream, largeLenght, network.subdomain, "large", extension);
				large.type = File.EXTERNAL;
				large.hash = largeHash;
				large.size = largeLenght;
				large.networkId = network.id;
				fileRepository.save(large);
			}

			image.small = small;
			image.medium = medium;
			image.large = large;

			image.smallHash = smallHash;
			image.mediumHash = mediumHash;
			image.largeHash = largeHash;
			image.originalHash = originalHash;

			image.vertical = bufferedImage.getHeight() > bufferedImage.getWidth();
		}
	}
}