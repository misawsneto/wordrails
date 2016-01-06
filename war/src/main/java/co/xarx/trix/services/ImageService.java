package co.xarx.trix.services;


import co.xarx.trix.domain.File;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Picture;
import co.xarx.trix.domain.QFile;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PictureRepository;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.ImageUtil;
import co.xarx.trix.util.TrixUtil;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.google.common.collect.Sets;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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


	public Image createNewImage(Image newImage, InputStream inputStream, String mime, boolean persistOnDatabase, boolean returnDuplicateIfExists) throws IOException, FileUploadException {
		Image.Type imageType = Image.Type.findByAbbr(newImage.type);
		if (imageType == null) {
			throw new BadRequestException("Image type is not valid");
		}

		Set<Picture> pictures = Sets.newHashSet();

		java.io.File originalFile = FileUtil.createNewTempFile(inputStream);
		ImageUtil.ImageFile imageFile = ImageUtil.getImageFile(originalFile);

		Image existingImage = getImageFromHashAndType(newImage.type, imageFile.hash);
		if (existingImage != null) {
			if(existingImage.pictures != null && !existingImage.pictures.isEmpty() &&
					existingImage.pictures.size() == imageType.count()+1) { //+1 because original
//				if(returnDuplicateIfExists) //uncomment when images become unique (when old image upload dies)
//					return existingImage;
//				else
					return createNewImageFromExistingImage(persistOnDatabase, existingImage, originalFile);
			}

		}

		Picture originalPic = getOriginalPicture(mime, originalFile, imageFile);
		pictures.add(originalPic);
		fileRepository.save(originalPic.file);

		if (imageType.qualities != null) {
			Map<String, Integer> qualities = imageType.qualities;
			for (Map.Entry<String, Integer> entry : qualities.entrySet()) {
				Picture pic = getPictureByQuality(originalFile, entry);
				pictures.add(pic);
				fileRepository.save(pic.file);
			}
		} else if (imageType.sizes != null) {
			Map<String, Integer[]> sizes = imageType.sizes;
			for (Map.Entry<String, Integer[]> entry : sizes.entrySet()) {
				Picture pic = getPictureBySize(originalFile, entry);
				pictures.add(pic);
				fileRepository.save(pic.file);
			}
		}

		newImage.vertical = imageFile.vertical;
		newImage.pictures = pictures;

		if(originalFile.exists())
			originalFile.delete();

		for (Picture picture : pictures) {
			pictureRepository.save(picture);
		}


		if (persistOnDatabase) {
			imageRepository.save(newImage);
		}

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

	public Image createNewImageFromExistingImage(boolean persistOnDatabase, Image existingImage, java.io.File originalFile) throws IOException, FileUploadException {
		Image newImage = new Image();
		newImage.type = existingImage.type;

		for (Picture pic : existingImage.pictures) {
			java.io.File tmpFile = FileUtil.createNewTempFile(new FileInputStream(originalFile));
			uploadIfDoesntExist(tmpFile, pic.file.hash, pic.file.size, pic.sizeTag, pic.file.getExtension());

			newImage.pictures.add(pic);
		}

		newImage.vertical = existingImage.vertical;

		if (persistOnDatabase) {
			imageRepository.save(newImage);
		}

		return newImage;
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

	private Picture getPictureBySize(java.io.File originalFile, Map.Entry<String, Integer[]> entry) throws IOException {
		ImageUtil.ImageFile imageFile;
		File existingFile;
		Picture pic = new Picture();
		pic.file = ImageUtil.createNewImageTrixFile("image/png", originalFile.length());
		pic.sizeTag = entry.getKey();

		Integer[] xy = entry.getValue();
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
		return pic;
	}

	private Picture getPictureByQuality(java.io.File originalFile, Map.Entry<String, Integer> entry) throws IOException {
		ImageUtil.ImageFile imageFile;
		File existingFile;
		Picture pic = new Picture();
		pic.file = ImageUtil.createNewImageTrixFile("image/png", originalFile.length());
		pic.sizeTag = entry.getKey();

		Integer quality = entry.getValue();
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
		return pic;
	}
}
