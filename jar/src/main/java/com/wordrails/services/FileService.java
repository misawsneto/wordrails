package com.wordrails.services;

import com.wordrails.business.BadRequestException;
import com.wordrails.util.WordrailsUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class FileService {

	@Autowired
	private AmazonCloudService amazonCloudService;

	public String newFile(String url, String domain, String mime) throws FileUploadException, IOException {
		return newFile(WordrailsUtil.getStreamFromUrl(url), domain, mime);
	}

	public String newFile(InputStream input, String domain, String mime) throws FileUploadException, IOException {
		long byteSize = ((FileInputStream) input).getChannel().size();

		return newFile(input, domain, mime, byteSize);
	}

	public String newResizedImage(BufferedImage image, String domain, Integer size, String sizeType, String mime) throws FileUploadException, IOException {
		File file = java.io.File.createTempFile("trix", "");

		BufferedImage bi = Thumbnails.of(image).size(size, size).outputFormat(mime).outputQuality(1).asBufferedImage();
		ImageIO.write(bi, mime, file);

		FileInputStream fis = new FileInputStream(file);
		long byteSize = fis.getChannel().size();

		return amazonCloudService.uploadPublicImage(fis, byteSize, domain, sizeType, mime);
	}

	public String newFile(InputStream inputStream, String domain, String mime, Long size) throws FileUploadException, BadRequestException {
		try {
			return amazonCloudService.uploadPublicImage(inputStream, size, domain, "original", mime);
		} catch (Exception e) {
			throw new FileUploadException("", e);
		}
	}

	public void updatePublicFile(InputStream inputStream, String domain, String fileName, Long size) throws FileUploadException, BadRequestException {
		try {
			amazonCloudService.uploadPublicImage(inputStream, size, domain, fileName);
		} catch (Exception e) {
			throw new FileUploadException("", e);
		}
	}
}
