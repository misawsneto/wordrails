package com.wordrails.business;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.persistence.AndroidIconRepository;
import com.wordrails.persistence.FileContentsRepository;
import com.wordrails.persistence.FileRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.util.TrixUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
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

@RepositoryEventHandler(AndroidIcon.class)
@Component
public class AndroidIconEventHandler {

	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private AndroidIconRepository androidIconRepository;
	@Autowired
	private FileContentsRepository fileContentsRepository;

	private Network network;
	private BufferedImage bufferedImage;
	private String mime;
	private String extension;

	@HandleBeforeCreate
	public void handleBeforeCreate(AndroidIcon icon) throws IOException, SQLException, FileUploadException {
		if (icon.icon == null) {
			throw new NullPointerException("icon icon can't be null");
		}

		FileContents originalFile = fileContentsRepository.findOne(icon.icon.id);
		if (originalFile.type.equals(File.EXTERNAL)) { //if is external, is already uploaded to amazon, so the file was uploaded before
//			List<AndroidIcon> existingImages = androidIconRepository.findByFile(originalFile.id);
//			AndroidIcon existingIcon = existingImages.get(0);
//			icon.mdpi = existingIcon.mdpi;
//			icon.hdpi = existingIcon.hdpi;
//			icon.xhdpi = existingIcon.xhdpi;
//			icon.xxhdpi = existingIcon.xxhdpi;
//			icon.xxxhdpi = existingIcon.xxxhdpi;
			return;
		}

		//if it's running here, this is a new image and needs to upload all sizes
		try (InputStream input = originalFile.contents.getBinaryStream()) {
			network = authProvider.getNetwork();

			java.io.File tmpFile = java.io.File.createTempFile(TrixUtil.generateRandomString(5, "aA#"), ".tmp");
			try {
				FileUtils.copyInputStreamToFile(input, tmpFile);
				bufferedImage = ImageIO.read(tmpFile);
				mime = originalFile.mime;
				extension = mime.split("/").length == 2 ? mime.split("/")[1] : "jpeg";

				int maxSize = Math.max(bufferedImage.getHeight(), bufferedImage.getWidth());

				String originalHash = amazonCloudService.uploadPublicImage(tmpFile, originalFile.size,
						network.subdomain, originalFile.hash, "icon", extension);

				originalFile.contents = null;
				originalFile.type = File.EXTERNAL;
				originalFile.hash = originalHash;
				if (originalFile.networkId == null || originalFile.networkId == 0) {
					originalFile.networkId = network.id;
				}
				fileContentsRepository.save(originalFile);

				File mdpi = uploadNewResizedImage(AndroidIcon.MDPI_SIZE, "mdpi");

				File hdpi;
				if (maxSize < AndroidIcon.HDPI_SIZE) {
					hdpi = icon.icon;
				} else {
					hdpi = uploadNewResizedImage(AndroidIcon.HDPI_SIZE, "hdpi");
				}

				File xhdpi;
				if (maxSize < AndroidIcon.HDPI_SIZE) {
					xhdpi = icon.icon;
				} else {
					xhdpi = uploadNewResizedImage(AndroidIcon.XHDPI_SIZE, "xhdpi");
				}

				File xxhdpi;
				if (maxSize < AndroidIcon.HDPI_SIZE) {
					xxhdpi = icon.icon;
				} else {
					xxhdpi = uploadNewResizedImage(AndroidIcon.XXHDPI_SIZE, "xxhdpi");
				}

				File xxxhdpi;
				if (maxSize < AndroidIcon.HDPI_SIZE) {
					xxxhdpi = icon.icon;
				} else {
					xxxhdpi = uploadNewResizedImage(AndroidIcon.XXXHDPI_SIZE, "xxxhdpi");
				}

				icon.mdpi = mdpi;
				icon.hdpi = hdpi;
				icon.xhdpi = xhdpi;
				icon.xxhdpi = xxhdpi;
				icon.xxxhdpi = xxxhdpi;
			} finally {
				if(tmpFile.exists()) {
					tmpFile.delete();
				}
			}
		}
	}

	private File uploadNewResizedImage(Integer resizePixels, String sizeTag) throws IOException {
		FileInputStream stream = resizeImage(bufferedImage, resizePixels, extension);
		long size = stream.getChannel().size();
		String hash = amazonCloudService.uploadPublicImage(stream, size, network.subdomain, sizeTag, extension);
		File file = new File();
		file.mime = mime;
		file.type = File.EXTERNAL;
		file.hash = hash;
		file.size = size;
		file.networkId = network.id;
		fileRepository.save(file);

		return file;
	}

	private FileInputStream resizeImage(BufferedImage image, Integer size, String mime) throws IOException {
		java.io.File file = java.io.File.createTempFile("trix", "");

		BufferedImage bi = Thumbnails.of(image).size(size, size).outputFormat(mime).outputQuality(1).asBufferedImage();
		ImageIO.write(bi, mime, file);

		return new FileInputStream(file);
	}
}