package com.wordrails.business;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.persistence.FileContentsRepository;
import com.wordrails.persistence.FileRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.services.FileService;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
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
	private static final String MIME = "image/jpeg";

	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private FileService fileService;

	@HandleBeforeCreate
	public void handleBeforeCreate(Image image) throws IOException, SQLException, FileUploadException {
		if(image.type == null || image.type.trim().isEmpty())
			image.type = Image.Type.POST.toString();

		if(!Image.containsType(image.type))
			throw new BadRequestException("Invalid Image Type:" + image.type);

		TrixFile original = image.original;
		if (original != null) {
			String mime = original.mime == null || original.mime.isEmpty() ? null : original.mime.split("image\\/").length == 2 ? original.mime.split("image\\/")[1] : null;

			Network network = authProvider.getNetwork();
			URL fullURL = new URL(amazonCloudService.getURL(network.domain, original.id + ""));
			HttpURLConnection connection = (HttpURLConnection) fullURL.openConnection();
			InputStream input =  connection.getInputStream();

			TrixFile small = fileService.newResizedImage(input, network.domain, 150, mime);
			TrixFile medium = fileService.newResizedImage(input, network.domain, 300, mime);
			TrixFile large = fileService.newResizedImage(input, network.domain, 1024, mime);

			image.small = small;
			image.medium = medium;
			image.large = large;

			BufferedImage bufferedImage = ImageIO.read(fullURL);
			image.vertical = bufferedImage.getHeight() > bufferedImage.getWidth();
		}
	}
}