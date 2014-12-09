package com.wordrails.business;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import net.coobird.thumbnailator.Thumbnails;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.LobCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.wordrails.persistence.FileContentsRepository;
import com.wordrails.persistence.FileRepository;

@RepositoryEventHandler(Image.class)
@Component
public class ImageEventHandler {
	private static final String MIME = "image/jpeg";
	private static final String FORMAT = "jpg";
	private static final double QUALITY = 0.9;
	
	private @PersistenceContext EntityManager manager;
	private @Autowired FileRepository fileRepository; 
	private @Autowired FileContentsRepository contentsRepository;

	@HandleBeforeCreate
	public void handleBeforeCreate(Image image) throws SQLException, IOException {
		com.wordrails.business.File original = image.original;
		
		String format = original.mime == null || original.mime.isEmpty() ? null : original.mime.split("image\\/").length == 2 ? original.mime.split("image\\/")[1] : null;
		
		if (original != null) {
			com.wordrails.business.File small = new File();
			small.type = File.INTERNAL_FILE; 
			small.mime = image.original.mime != null ? image.original.mime : MIME;
			small.name = original.name;			
			fileRepository.save(small);
			
			com.wordrails.business.File medium = new File();
			medium.type = File.INTERNAL_FILE;
			medium.mime = image.original.mime != null ? image.original.mime : MIME;
			medium.name = original.name;			
			fileRepository.save(medium);
			
			com.wordrails.business.File large = new File();
			large.type = File.INTERNAL_FILE;
			large.mime = image.original.mime != null ? image.original.mime : MIME;
			large.name = original.name;
			fileRepository.save(large);

			image.small = small;
			image.medium = medium;
			image.large = large;

			BufferedImage bufferedImage;
			FileContents contents = contentsRepository.findOne(original.id);
			try (InputStream input = contents.contents.getBinaryStream()) {
				bufferedImage = ImageIO.read(input);
			}
			updateContents(small.id, bufferedImage, 150, format);
			updateContents(medium.id, bufferedImage, 300, format);
			updateContents(large.id, bufferedImage, 1024, format);
		}
	}

	private void updateContents(Integer id, BufferedImage image, int size, String format) throws IOException {
		format = (format != null  ? format : FORMAT);
		java.io.File file = java.io.File.createTempFile("image", "." + format);
		try {
			Thumbnails.of(image).size(size, size).outputFormat(format).outputQuality(QUALITY).toFile(file);			
			try (InputStream input = new FileInputStream(file)) {
				Session session = (Session) manager.getDelegate();
				LobCreator creator = Hibernate.getLobCreator(session);
				FileContents contents = contentsRepository.findOne(id);
				contents.contents = creator.createBlob(input, file.length());
				contentsRepository.save(contents);
			}					
		} finally {
			file.delete();
		}
	}
}