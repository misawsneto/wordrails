package co.xarx.trix.services;


import co.xarx.trix.domain.*;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PictureRepository;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.ImageUtil;
import co.xarx.trix.util.TrixUtil;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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


	@Cacheable(value = "image", key = "#p0")
	public Map<String, String> getHashes(String originalHash) throws EntityNotFoundException {
		List<Image> images = Lists.newArrayList(imageRepository.findAll(QImage.image.originalHash.eq(originalHash)));

		if(CollectionUtils.isEmpty(images)) {
			throw new EntityNotFoundException("Image does not exist");
		} else {
			return images.get(0).hashs;
		}
	}


	public Image createNewImage(String type, InputStream inputStream, String mime, boolean persistOnDatabase, boolean returnDuplicateIfExists) throws IOException, FileUploadException {
		Image newImage = new Image();
		newImage.type = type;

		Image.Type imageType = Image.Type.findByAbbr(type);
		if (imageType == null) {
			throw new BadRequestException("Image type is not valid");
		}

		Set<Picture> pictures = Sets.newHashSet();

		java.io.File originalFile = FileUtil.createNewTempFile(inputStream);
		ImageUtil.ImageFile imageFile = ImageUtil.getImageFile(originalFile);

		Image existingImage = getImageFromHashAndType(type, imageFile.hash);
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
			for (Image i : images) {
				if (i.type.equals(type)) {
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
			if (!TrixUtil.urlExists(amazonCloudService.getPublicImageURL(pic.file.hash))) { //if image doesnt exist on amazon servers, upload it. it should exist
				java.io.File tmpFile = FileUtil.createNewTempFile(new FileInputStream(originalFile));
				try {
					amazonCloudService.uploadPublicImage(tmpFile, pic.file.size,
							pic.file.hash, pic.sizeTag, pic.file.getExtension(), true);
				} catch (AmazonS3Exception e) {
					throw new FileUploadException("Error uploading image to s3", e);
				}
			}

			newImage.pictures.add(pic);
		}

		newImage.vertical = existingImage.vertical;

		if (persistOnDatabase) {
			imageRepository.save(newImage);
		}

		return newImage;
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
