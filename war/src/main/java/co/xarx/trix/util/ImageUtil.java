package co.xarx.trix.util;


import co.xarx.trix.domain.File;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

public class ImageUtil {

	public static File createNewImageTrixFile(String mime, Long size) {
		File file = new File();
		file.directory = File.DIR_IMAGES;
		file.type = File.EXTERNAL;
		file.mime = mime;
		file.size = size;

		return file;
	}

	public static ImageFile resizeImage(InputStream inputStream, Integer height, Integer width, String extension) throws IOException {
		java.io.File file = FileUtil.createNewTempFile(inputStream);
		return resizeImage(file, height, width, extension);
	}

	public static ImageFile resizeImage(java.io.File file, Integer height, Integer width, String extension) throws IOException {
		java.io.File newFile = FileUtil.createNewTempFile();
		BufferedImage bi = Thumbnails.of(file).forceSize(width, height).outputFormat("png").outputQuality(1).asBufferedImage();
		ImageIO.write(bi, "png", newFile);

		return new ImageFile(newFile, bi.getHeight(), bi.getWidth(), FileUtil.getHash(new FileInputStream(newFile)));
	}

	public static ImageFile resizeImage(InputStream inputStream, Integer quality, String extension, boolean resizeUp, boolean resizeDown) throws IOException {
		java.io.File file = FileUtil.createNewTempFile(inputStream);
		return resizeImage(file, quality, extension, resizeUp, resizeDown);
	}

	public static ImageFile resizeImage(java.io.File file, Integer quality, String extension, boolean resizeUp, boolean resizeDown) throws IOException {
		java.io.File newFile = FileUtil.createNewTempFile();
		BufferedImage bi = ImageIO.read(file);
		if(bi == null) {
			// favicon TODO-ARTHUR: this needs security and validation
			return new ImageFile(file, 32, 32, FileUtil.getHash(new FileInputStream(file)));
		}

		Double currentQuality = Math.sqrt(bi.getHeight() * bi.getWidth());
		if ((resizeUp && quality > currentQuality) || (resizeDown && quality < currentQuality)) {
			Double pctResize = new BigDecimal(quality).divide(new BigDecimal(currentQuality), 5, BigDecimal.ROUND_HALF_UP).doubleValue();

			bi = Thumbnails.of(bi).scale(pctResize).outputFormat("png").asBufferedImage();
			ImageIO.write(bi, "png", newFile);
		} else {
			newFile = file;
		}

		return new ImageFile(newFile, bi.getHeight(), bi.getWidth(), FileUtil.getHash(new FileInputStream(newFile)));
	}

	public static ImageFile getImageFile(java.io.File file) throws IOException {
		if(file == null) return null;

		BufferedImage bi = ImageIO.read(file);
		if(bi == null){
			// favicon TODO-ARTHUR: this needs security and validation
			return new ImageFile(file, 32, 32, FileUtil.getHash(new FileInputStream(file)));
		}
		return new ImageFile(file, bi.getHeight(), bi.getWidth(), FileUtil.getHash(new FileInputStream(file)));
	}

	public static class ImageFile {
		public java.io.File file;
		public Integer height;
		public Integer width;
		public String hash;
		public boolean vertical;

		public ImageFile(java.io.File file, Integer height, Integer width, String hash) {
			this.file = file;
			this.height = height;
			this.width = width;
			this.hash = hash;
			this.vertical = height > width;
		}
	}
}
