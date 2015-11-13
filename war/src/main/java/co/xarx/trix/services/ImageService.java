package co.xarx.trix.services;


import co.xarx.trix.domain.BadRequestException;
import co.xarx.trix.domain.File;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Picture;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PictureRepository;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.TrixUtil;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.google.common.collect.Sets;
import co.xarx.trix.util.ImageUtil;
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

	public Image createNewImage(String type, String networkSubdomain, InputStream inputStream, String mime, Integer networkId, boolean persistOnDatabase, boolean returnDuplicateIfExists) throws IOException, FileUploadException {
		Image newImage = new Image();
		newImage.type = type;

		Image.Type imageType = Image.Type.findByAbbr(type);
		if (imageType == null) {
			throw new BadRequestException("Image type is not valid");
		}

		Set<Picture> pictures = Sets.newHashSet();

		java.io.File originalFile = FileUtil.createNewTempFile(inputStream);
		String hash = FileUtil.getHash(new FileInputStream(originalFile));

		Image existingImage = getImageFromHashAndType(type, hash, networkId);
		if (existingImage != null) {
			existingImage.pictures = pictureRepository.findByImage(existingImage);
			if(existingImage.pictures != null && !existingImage.pictures.isEmpty() &&
					existingImage.pictures.size() == imageType.count()+1) { //+1 because original
//				if(returnDuplicateIfExists) //uncomment when images become unique (when old image upload dies)
//					return existingImage;
//				else
					return createNewImageFromExistingImage(networkSubdomain, persistOnDatabase, existingImage, originalFile);
			}

		}



		ImageUtil.ImageFile imageFile = ImageUtil.getImageFile(originalFile);
		Picture originalPic = getOriginalPicture(networkSubdomain, mime, networkId, hash, originalFile, imageFile);
		pictures.add(originalPic);
		fileRepository.save(originalPic.file);

		if (imageType.qualities != null) {
			Map<String, Integer> qualities = imageType.qualities;
			for (Map.Entry<String, Integer> entry : qualities.entrySet()) {
				Picture pic = getPictureByQuality(networkSubdomain, networkId, originalFile, entry);
				pictures.add(pic);
				fileRepository.save(pic.file);
			}
		} else if (imageType.sizes != null) {
			Map<String, Integer[]> sizes = imageType.sizes;
			for (Map.Entry<String, Integer[]> entry : sizes.entrySet()) {
				Picture pic = getPictureBySize(networkSubdomain, networkId, originalFile, entry);
				pictures.add(pic);
				fileRepository.save(pic.file);
			}
		}

		newImage.vertical = imageFile.vertical;
		newImage.pictures = pictures;

		if(originalFile.exists())
			originalFile.delete();


		for(Picture pic : pictures) {
			pic.image = newImage;
		}

		if (persistOnDatabase) {
			imageRepository.save(newImage);
		}

		return newImage;
	}

	public Image getImageFromHashAndType(String type, String hash, Integer networkId) {
		Image existingImage = null;
		Set<Image> images = imageRepository.findByFileHashFetchPictures(hash, networkId);
		if (images != null && !images.isEmpty()) {
			for(Image i : images) {
				if(i.type.equals(type)) {
					existingImage = i;
				}
			}
		}
		return existingImage;
	}

	public Image createNewImageFromExistingImage(String networkSubdomain, boolean persistOnDatabase, Image existingImage, java.io.File originalFile) throws IOException, FileUploadException {
		Image newImage = new Image();
		newImage.type = existingImage.type;

		for (Picture pic : existingImage.pictures) {
			if (!TrixUtil.urlExists(amazonCloudService.getPublicImageURL(networkSubdomain, pic.file.hash))) { //if image doesnt exist on amazon servers, upload it. it should exist
				java.io.File tmpFile = FileUtil.createNewTempFile(new FileInputStream(originalFile));
				try {
					amazonCloudService.uploadPublicImage(tmpFile, pic.file.size,
							networkSubdomain, pic.file.hash, pic.sizeTag, pic.file.getExtension(), true);
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

	private Picture getOriginalPicture(String networkSubdomain, String mime, Integer networkId, String hash, java.io.File originalFile, ImageUtil.ImageFile imageFile) throws IOException {
		Picture originalPic = new Picture();
		originalPic.file = ImageUtil.createNewImageTrixFile(networkId, mime, originalFile.length());
		originalPic.sizeTag = Image.SIZE_ORIGINAL;
		originalPic.networkId = networkId;

		File existingFile = fileRepository.findByHashAndNetworkId(imageFile.hash, networkId);
		if(existingFile != null) {
			originalPic.file = existingFile;
		} else {
			hash = amazonCloudService.uploadPublicImage(originalFile, originalFile.length(), networkSubdomain, null, originalPic.sizeTag, originalPic.file.getExtension(), false);
		}

		originalPic.file.hash = hash;
		originalPic.height = imageFile.height;
		originalPic.width = imageFile.width;
		return originalPic;
	}

	private Picture getPictureBySize(String networkSubdomain, Integer networkId, java.io.File originalFile, Map.Entry<String, Integer[]> entry) throws IOException {
		ImageUtil.ImageFile imageFile;
		File existingFile;
		Picture pic = new Picture();
		pic.file = ImageUtil.createNewImageTrixFile(networkId, "image/png", originalFile.length());
		pic.sizeTag = entry.getKey();
		pic.networkId = networkId;

		Integer[] xy = entry.getValue();
		imageFile = ImageUtil.resizeImage(originalFile, xy[0], xy[1], pic.file.getExtension());

		existingFile = fileRepository.findByHashAndNetworkId(imageFile.hash, networkId);
		if(existingFile != null) {
			pic.file = existingFile;
		} else {
			amazonCloudService.uploadPublicImage(imageFile.file,
					imageFile.file.length(), networkSubdomain, imageFile.hash, pic.sizeTag, pic.file.getExtension(), false);
		}

		pic.file.size = imageFile.file.length();
		pic.file.hash = imageFile.hash;
		pic.height = imageFile.height;
		pic.width = imageFile.width;
		return pic;
	}

	private Picture getPictureByQuality(String networkSubdomain, Integer networkId, java.io.File originalFile, Map.Entry<String, Integer> entry) throws IOException {
		ImageUtil.ImageFile imageFile;
		File existingFile;
		Picture pic = new Picture();
		pic.file = ImageUtil.createNewImageTrixFile(networkId, "image/png", originalFile.length());
		pic.sizeTag = entry.getKey();
		pic.networkId = networkId;

		Integer quality = entry.getValue();
		imageFile = ImageUtil.resizeImage(originalFile, quality, pic.file.getExtension(), false, true);

		existingFile = fileRepository.findByHashAndNetworkId(imageFile.hash, networkId);
		if(existingFile != null) {
			pic.file = existingFile;
		} else {
			amazonCloudService.uploadPublicImage(imageFile.file,
					imageFile.file.length(), networkSubdomain, imageFile.hash, pic.sizeTag, pic.file.getExtension(), false);
		}

		pic.file.size = imageFile.file.length();
		pic.file.hash = imageFile.hash;
		pic.height = imageFile.height;
		pic.width = imageFile.width;
		return pic;
	}
}
