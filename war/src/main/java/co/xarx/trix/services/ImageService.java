package co.xarx.trix.services;


import co.xarx.trix.domain.File;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Picture;
import co.xarx.trix.domain.QFile;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PictureRepository;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.ImageUtil;
import co.xarx.trix.util.TrixUtil;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class ImageService {

	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private PictureRepository pictureRepository;

	public ImageService() {
	}


	public Image createNewImage(Image newImage, InputStream inputStream, String mime) throws IOException, FileUploadException {
		Image.Type imageType = Image.Type.findByAbbr(newImage.type);

		Assert.isTrue(imageType != null, "Image type is not valid");

		Set<Picture> pictures = new HashSet<>();

		java.io.File originalFile = FileUtil.createNewTempFile(inputStream);
		ImageUtil.ImageFile imageFile = ImageUtil.getImageFile(originalFile);

		Image existingImage = getImageFromHashAndType(newImage.type, imageFile.hash);
		if (existingImage != null) {
			if(existingImage.pictures != null && !existingImage.pictures.isEmpty() &&
					existingImage.pictures.size() == imageType.count()+1) { //+1 because original
				return existingImage;
			} else {
				newImage = existingImage;
			}
		}

		Picture originalPic = getOriginalPicture(mime, originalFile, imageFile);
		pictures.add(originalPic);
		fileRepository.save(originalPic.file);

		if (imageType.qualities != null) {
			Map<String, Integer> qualities = imageType.qualities;
			for (Map.Entry<String, Integer> entry : qualities.entrySet()) {
				Picture pic = getPictureByQuality(originalFile, entry.getKey(), entry.getValue());
				pictures.add(pic);

			}
		} else if (imageType.sizes != null) {
			Map<String, Integer[]> sizes = imageType.sizes;
			for (Map.Entry<String, Integer[]> entry : sizes.entrySet()) {
				Picture pic = getPictureBySize(originalFile, entry.getKey(), entry.getValue());
				pictures.add(pic);
			}
		}

		newImage.vertical = imageFile.vertical;
		newImage.pictures = pictures;

		if(originalFile.exists())
			originalFile.delete();

		for (Picture picture : pictures) {
			pictureRepository.save(picture);
		}


		imageRepository.save(newImage);

		return newImage;
	}

	public Image getImageFromHashAndType(String type, String hash) {
		Image existingImage = null;
		Set<Image> images = imageRepository.findByFileHashFetchPictures(hash);
		if (images != null && !images.isEmpty()) {
			for(Image i : images) {
				if(i.type.equals(type)) {
					existingImage = i;
				}
			}
		}
		return existingImage;
	}

	public void uploadIfDoesntExist(java.io.File file, String hash, Long size, String sizeTag, String extension) throws IOException, FileUploadException {
		if(hash == null) hash = FileUtil.getHash(new FileInputStream(file));
		if(size == null) size = file.length();
		if(extension == null) extension = "png";
		if (!TrixUtil.urlExists(amazonCloudService.getPublicImageURL(hash))) { //if image doesnt exist on amazon servers, upload it. it should exist
			try {
				amazonCloudService.uploadPublicImage(file, size,
						hash, sizeTag, extension, true);
			} catch (AmazonS3Exception e) {
				throw new FileUploadException("Error uploading image to s3", e);
			}
		}
	}

	private Picture getOriginalPicture(String mime, java.io.File originalFile, ImageUtil.ImageFile imageFile) throws IOException {
		Picture originalPic = new Picture();
		originalPic.file = ImageUtil.createNewImageTrixFile(mime, originalFile.length());
		originalPic.sizeTag = Image.SIZE_ORIGINAL;

		File existingFile = fileRepository.findOne(QFile.file.hash.eq(imageFile.hash));
		if(existingFile != null) {
			originalPic.file = existingFile;
		}

		String hash = null;
		if(originalPic.file.id == null || originalPic.file.type.equals(File.INTERNAL)) {
			hash = amazonCloudService.uploadPublicImage(originalFile, originalFile.length(), null, originalPic.sizeTag, originalPic.file.getExtension(), false);
		}

		if(originalPic.file.hash == null || originalPic.file.hash.isEmpty()) {
			originalPic.file.hash = hash;
		}

		originalPic.height = imageFile.height;
		originalPic.width = imageFile.width;
		return originalPic;
	}

	private Picture getPictureBySize(java.io.File originalFile, String sizeTag, Integer[] xy) throws IOException {
		ImageUtil.ImageFile imageFile;
		File existingFile;
		Picture pic = new Picture();
		pic.file = ImageUtil.createNewImageTrixFile("image/png", originalFile.length());
		pic.sizeTag = sizeTag;

		imageFile = ImageUtil.resizeImage(originalFile, xy[0], xy[1], pic.file.getExtension());

		existingFile = fileRepository.findOne(QFile.file.hash.eq(imageFile.hash).and(QFile.file.type.eq(File.EXTERNAL)));
		if(existingFile != null) {
			pic.file = existingFile;
		} else {
			amazonCloudService.uploadPublicImage(imageFile.file,
					imageFile.file.length(), imageFile.hash, pic.sizeTag, pic.file.getExtension(), false);
		}

		pic.file.size = imageFile.file.length();
		pic.file.hash = imageFile.hash;
		pic.height = imageFile.height;
		pic.width = imageFile.width;

		try {
			fileRepository.save(pic.file);
		} catch (Exception e) {
		}

		return pic;
	}

	private Picture getPictureByQuality(java.io.File originalFile, String sizeTag, Integer quality) throws IOException {
		ImageUtil.ImageFile imageFile;
		File existingFile;
		Picture pic = new Picture();
		pic.file = ImageUtil.createNewImageTrixFile("image/png", originalFile.length());
		pic.sizeTag = sizeTag;

		imageFile = ImageUtil.resizeImage(originalFile, quality, pic.file.getExtension(), false, true);

		existingFile = fileRepository.findOne(QFile.file.hash.eq(imageFile.hash).and(QFile.file.type.eq(File.EXTERNAL)));
		if(existingFile != null) {
			pic.file = existingFile;
		} else {
			amazonCloudService.uploadPublicImage(imageFile.file,
					imageFile.file.length(), imageFile.hash, pic.sizeTag, pic.file.getExtension(), false);

		}

		pic.file.size = imageFile.file.length();
		pic.file.hash = imageFile.hash;
		pic.height = imageFile.height;
		pic.width = imageFile.width;

		try {
			fileRepository.save(pic.file);
		} catch (Exception e) {
		}

		return pic;
	}
}
