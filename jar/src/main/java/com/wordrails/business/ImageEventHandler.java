package com.wordrails.business;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.services.FileService;
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

	@HandleBeforeCreate
	public void handleBeforeCreate(Image image) throws IOException, SQLException, FileUploadException {
		if (image.type == null || image.type.trim().isEmpty()) image.type = Image.Type.POST.toString();

		if (!Image.containsType(image.type)) throw new BadRequestException("Invalid Image Type:" + image.type);

		String original = image.originalId;
		if (original != null) {
			Network network = authProvider.getNetwork();
			URL fullURL = new URL(amazonCloudService.getURL(network.domain, original));
			HttpURLConnection connection = (HttpURLConnection) fullURL.openConnection();
			InputStream input = connection.getInputStream();

			String mime = new Tika().detect(input);
//			String mime = original.mime == null || original.mime.isEmpty() ? null : original.mime.split("image\\/").length == 2 ? original.mime.split("image\\/")[1] : null;

			BufferedImage bufferedImage = ImageIO.read(fullURL);

			String small = fileService.newResizedImage(bufferedImage, network.domain, 150, "small", mime);
			String medium = fileService.newResizedImage(bufferedImage, network.domain, 300, "medium", mime);
			String large = fileService.newResizedImage(bufferedImage, network.domain, 1024, "large", mime);

			image.smallId = small;
			image.mediumId = medium;
			image.largeId = large;

			image.vertical = bufferedImage.getHeight() > bufferedImage.getWidth();
		}
	}
}