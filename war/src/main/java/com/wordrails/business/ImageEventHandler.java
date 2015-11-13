package com.wordrails.business;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.persistence.FileContentsRepository;
import com.wordrails.persistence.FileRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.util.TrixUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

@RepositoryEventHandler(Image.class)
@Component
public class ImageEventHandler {

	Logger log = Logger.getLogger(ImageEventHandler.class.getName());

	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private FileContentsRepository fileContentsRepository;

	private Network network;
	private BufferedImage bufferedImage;
	private String mime;
	private String extension;

	@HandleBeforeCreate
	public void handleBeforeCreate(Image image) throws IOException, SQLException, FileUploadException {
		if (image.type == null || image.type.trim().isEmpty())
			image.type = Image.Type.POST.toString();

		if (image.original == null) {
			throw new NullPointerException("original icon can't be null");
		}

		network = authProvider.getNetwork();
		FileContents originalFile = fileContentsRepository.findOne(image.original.id);
		//if is external, is already uploaded to amazon, so the file was uploaded before
		if (originalFile.type.equals(File.EXTERNAL) && amazonCloudService.exists(network.subdomain, originalFile.hash)) {
			List<Image> existingImages = imageRepository.findByFileId(originalFile.id);
			Image existingImage = existingImages.get(0);
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

			java.io.File tmpFile = java.io.File.createTempFile(TrixUtil.generateRandomString(5, "aA#"), ".tmp");
			try {
				FileUtils.copyInputStreamToFile(input, tmpFile);
				bufferedImage = ImageIO.read(tmpFile);
				mime = originalFile.mime;
				extension = mime.split("/").length == 2 ? mime.split("/")[1] : "jpeg";

				int maxSize = Math.max(bufferedImage.getHeight(), bufferedImage.getWidth());

				File small = uploadNewResizedImage(150, "small");

				File medium;
				if (maxSize < 250) {
					medium = image.original;
				} else {
					medium = uploadNewResizedImage(300, "medium");
				}

				File large;
				if (maxSize < 800) {
					large = image.original;
				} else {
					int largeSize = 1024;
					if (maxSize >= 1200 && maxSize < 1500) {
						largeSize = 1400;
					} else if (maxSize >= 1500) {
						largeSize = 1600;
					}

					large = uploadNewResizedImage(largeSize, "large");
				}

				String originalHash = amazonCloudService.uploadPublicImage(tmpFile, originalFile.size,
						network.subdomain, originalFile.hash, "original", extension);

				originalFile.contents = null;
				originalFile.type = File.EXTERNAL;
				originalFile.hash = originalHash;
				if (originalFile.networkId == null || originalFile.networkId == 0) {
					originalFile.networkId = network.id;
				}
				fileContentsRepository.save(originalFile);

				image.small = small;
				image.medium = medium;
				image.large = large;

				image.smallHash = small.hash;
				image.mediumHash = medium.hash;
				image.largeHash = large.hash;
				image.originalHash = originalHash;

				image.vertical = bufferedImage.getHeight() > bufferedImage.getWidth();

			} catch(AmazonS3Exception e) {
				log.error("Error uploading image to s3", e);
				throw e;
			} finally {
				if(tmpFile.exists()) {
					tmpFile.delete();
				}
			}
		}
	}

	private File uploadNewResizedImage(Integer resizePixels, String sizeTag) throws IOException {
		FileInputStream stream = resizeImage(bufferedImage, resizePixels);
		long size = stream.getChannel().size();
		String hash = amazonCloudService.uploadPublicImage(stream, size, network.subdomain, sizeTag, extension);
		File file = new File();
		file.mime = mime;
		file.type = File.EXTERNAL;
		file.hash = hash;
		file.size = size;
		file.directory = "images";
		file.networkId = network.id;
		fileRepository.save(file);

		return file;
	}

	private FileInputStream resizeImage(BufferedImage image, Integer size) throws IOException {
		java.io.File file = java.io.File.createTempFile("trix", "");

		BufferedImage bi = Thumbnails.of(image).size(size, size).outputFormat(extension).outputQuality(1).asBufferedImage();
		ImageIO.write(bi, extension, file);

		return new FileInputStream(file);
	}
}