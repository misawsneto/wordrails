package com.wordrails.services;

import com.wordrails.business.BadRequestException;
import com.wordrails.business.TrixFile;
import com.wordrails.persistence.FileRepository;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FileUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class FileService {


	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private AmazonCloudService amazonCloudService;

	public TrixFile newFile(String url, String domain) throws FileUploadException, IOException {
		URL fullURL = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) fullURL.openConnection();
		InputStream input = connection.getInputStream();

		return newFile(input, domain);
	}

	public TrixFile newFile(InputStream input, String domain) throws FileUploadException, IOException {
		File file = java.io.File.createTempFile("trix", "");
		FileUtils.copyInputStreamToFile(input, file);
		String mime = new Tika().detect(input);

		return newFile(input, domain, mime, "", file.length());
	}

	public TrixFile newResizedImage(InputStream input, String domain, Integer size, String mime) throws FileUploadException, IOException {
		File file = java.io.File.createTempFile("trix", "");
		BufferedImage bi = Thumbnails.of(input).size(size, size).outputFormat(mime).outputQuality(1).asBufferedImage();
		ImageIO.write(bi, mime, file);

		return newFile(input, domain, mime, "", file.length());
	}

	public TrixFile newFile(InputStream inputStream, String domain, String mime, String name, Long size) throws FileUploadException, BadRequestException {
		TrixFile trixFile;
		try {
			trixFile = new TrixFile();
			trixFile.type = TrixFile.EXTERNAL_FILE;
			trixFile.mime = mime;
			trixFile.name = name;
			fileRepository.save(trixFile);

			String fileName = trixFile.id.toString();
			amazonCloudService.uploadPublicImage(inputStream, size, domain, fileName, "original", trixFile.mime);

			return trixFile;
		} catch (Exception e) {
			throw new FileUploadException("", e);
		}
	}
}
