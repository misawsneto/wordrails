package com.wordrails.business;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.persistence.FileRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.services.FileService;
import com.wordrails.util.WordrailsUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.tika.Tika;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.LobCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
	private @PersistenceContext EntityManager manager;

	@HandleBeforeCreate
	public void handleBeforeCreate(Image image) throws IOException, SQLException, FileUploadException {
		LobCreator creator = Hibernate.getLobCreator((Session) manager.getDelegate());

		if (image.type == null || image.type.trim().isEmpty()) image.type = Image.Type.POST.toString();

		if (!Image.containsType(image.type)) throw new BadRequestException("Invalid Image Type:" + image.type);

		File originalFile = image.original;
		String originalHash = originalFile.hash;
		if (originalHash != null) {
			Network network = authProvider.getNetwork();
			String url = amazonCloudService.getPublicImageURL(network.subdomain, originalHash);
			InputStream input = WordrailsUtil.getStreamFromUrl(url);

			String mime = new Tika().detect(input);
			mime = mime.split("/").length == 2 ? mime.split("/")[1] : "jpeg";

			BufferedImage bufferedImage = ImageIO.read(new URL(url));

			int maxSize = Math.max(bufferedImage.getHeight(), bufferedImage.getWidth());

			String smallHash = fileService.newResizedImage(bufferedImage, network.subdomain, 150, "small", mime);
			File small = new File(smallHash);
			fileRepository.save(small);

			File medium;
			String mediumHash;
			if(maxSize < 250) {
				mediumHash = originalHash;
				medium = image.original;
			} else {
				mediumHash = fileService.newResizedImage(bufferedImage, network.subdomain, 300, "medium", mime);
				medium = new File(mediumHash);
				fileRepository.save(medium);
			}

			File large;
			String largeHash;
			if(maxSize < 800) {
				largeHash = originalHash;
				large = image.original;
			} else {
				int largeSize = 1024;
				if(maxSize >= 1200 && maxSize < 1500) {
					largeSize = 1400;
				} else if(maxSize >= 1500) {
					largeSize = 1600;
				}

				largeHash = fileService.newResizedImage(bufferedImage, network.subdomain, largeSize, "large", mime);
				large = new File(largeHash);
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

	public void updateContents(LobCreator creator, Integer id, BufferedImage image, int size, String format) throws IOException {
		format = (format != null  ? format : "jpg");
		java.io.File file = java.io.File.createTempFile("image", "." + format);
		try {
			Thumbnails.of(image).size(size, size).outputFormat(format).outputQuality(1.0).toFile(file);
			try (InputStream input = new FileInputStream(file)) {
				File contents = fileRepository.findOne(id);
				contents.contents = creator.createBlob(input, file.length());
				fileRepository.save(contents);
			}
		} finally {
			file.delete();
		}
	}
}