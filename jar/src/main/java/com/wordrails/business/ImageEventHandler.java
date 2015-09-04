package com.wordrails.business;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.persistence.FileRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.services.FileService;
import com.wordrails.util.WordrailsUtil;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.SQLException;

@RepositoryEventHandler(Image.class)
@Component
public class ImageEventHandler {

	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private FileService fileService;
	@Autowired
	private FileRepository fileRepository;

	@HandleBeforeCreate
	public void handleBeforeCreate(Image image) throws IOException, SQLException, FileUploadException {
		if (image.type == null || image.type.trim().isEmpty()) image.type = Image.Type.POST.toString();

		if (!Image.containsType(image.type)) throw new BadRequestException("Invalid Image Type:" + image.type);

		TrixFile originalFile = image.original;
		String originalHash = originalFile.hash;
		if (originalHash != null) {
			Network network = authProvider.getNetwork();
			String url = amazonCloudService.getURL(network.domain, originalHash);
			InputStream input = WordrailsUtil.getStreamFromUrl(url);

			String mime = new Tika().detect(input);
			mime = mime.split("/").length == 2 ? mime.split("/")[1] : "jpeg";

			BufferedImage bufferedImage = ImageIO.read(new URL(url));

			int maxSize = Math.max(bufferedImage.getHeight(), bufferedImage.getWidth());

			String smallHash = fileService.newResizedImage(bufferedImage, network.domain, Math.max(maxSize, 150), "small", mime);
			TrixFile small = new TrixFile(smallHash);
			fileRepository.save(small);

			TrixFile medium;
			String mediumHash;
			if(maxSize < 300) {
				mediumHash = smallHash;
				medium = small;
			} else {
				mediumHash = fileService.newResizedImage(bufferedImage, network.domain, 300, "medium", mime);
				medium = new TrixFile(mediumHash);
				fileRepository.save(medium);
			}

			TrixFile large;
			String largeHash;
			if(maxSize < 1024) {
				largeHash = mediumHash;
				large = medium;
			} else {
				largeHash = fileService.newResizedImage(bufferedImage, network.domain, 300, "medium", mime);
				large = new TrixFile(largeHash);
				fileRepository.save(large);
			}

			image.small = small;
			image.medium = medium;
			image.large = large;

			image.smallHash = smallHash;
			image.mediumHash = mediumHash;
			image.largeHash = largeHash;

			image.vertical = bufferedImage.getHeight() > bufferedImage.getWidth();
		}
	}
}