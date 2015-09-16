package com.wordrails.services;

import com.wordrails.business.BadRequestException;
import com.wordrails.util.WordrailsUtil;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@Component
public class FileService {

	@Autowired
	private AmazonCloudService amazonCloudService;

	public String newFile(String url, String domain, String mime) throws FileUploadException, IOException {
		return newFile(WordrailsUtil.getStreamFromUrl(url), domain, mime);
	}

	public String newFile(InputStream input, String domain, String mime) throws FileUploadException, IOException {
		long byteSize = IOUtils.toByteArray(input).length;
		return amazonCloudService.uploadPublicImage(input, byteSize, domain, "original", mime);
	}

	public FileInputStream resizeImage(BufferedImage image, Integer size, String mime) throws IOException {
		File file = java.io.File.createTempFile("trix", "");

		BufferedImage bi = Thumbnails.of(image).size(size, size).outputFormat(mime).outputQuality(1).asBufferedImage();
		ImageIO.write(bi, mime, file);

		return new FileInputStream(file);
	}

	public String newResizedImage(BufferedImage image, String domain, Integer size, String sizeType, String mime) throws FileUploadException, IOException {
		File file = java.io.File.createTempFile("trix", "");

		BufferedImage bi = Thumbnails.of(image).size(size, size).outputFormat(mime).outputQuality(1).asBufferedImage();
		ImageIO.write(bi, mime, file);

		FileInputStream fis = new FileInputStream(file);
		long byteSize = fis.getChannel().size();

		return amazonCloudService.uploadPublicImage(fis, byteSize, domain, sizeType, mime);
	}
}
