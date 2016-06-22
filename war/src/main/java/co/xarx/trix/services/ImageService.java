package co.xarx.trix.services;


import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PictureRepository;
import co.xarx.trix.util.FileUtil;
import co.xarx.trix.util.ImageFile;
import co.xarx.trix.util.ImageUtil;
import com.google.common.collect.Lists;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

@Service
public class ImageService {

	private ImageRepository imageRepository;
	private AmazonCloudService amazonCloudService;
	private FileRepository fileRepository;
	private PictureRepository pictureRepository;
	private ImageUtil imageUtil;
	private FileService fileService;

	@Autowired
	public ImageService(ImageRepository imageRepository, AmazonCloudService amazonCloudService, FileRepository
			fileRepository, PictureRepository pictureRepository, ImageUtil imageUtil, FileService fileService) {
		this.imageRepository = imageRepository;
		this.amazonCloudService = amazonCloudService;
		this.fileRepository = fileRepository;
		this.pictureRepository = pictureRepository;
		this.imageUtil = imageUtil;
		this.fileService = fileService;
	}


	@Cacheable(value = "image", key = "#p0")
	public Map<String, String> getHashes(String originalHash) throws EntityNotFoundException {
		List<Image> images = Lists.newArrayList(imageRepository.findAll(QImage.image.originalHash.eq(originalHash)));

		if (CollectionUtils.isEmpty(images)) {
			throw new EntityNotFoundException("Image does not exist");
		}

		return images.get(0).getHashes();
	}

	@Transactional
	public Image createAndSaveNewImage(String type, String name, java.io.File originalFile, String mime) throws Exception {
		mime = mime != null && mime.contains("octet-stream") ? FileUtil.getMimeTypeFromName(name) : mime;
		Image image = createNewImage(type, name, originalFile, mime);

		for (Picture picture : image.getPictures()) {

			if (picture.getFile().getId() == null) {
				File temp = fileRepository.findOne(QFile.file.hash.eq(picture.file.hash).and(QFile.file.type.eq(File.EXTERNAL)));
				if (temp == null) {

					if(Image.SIZE_ORIGINAL.equals(picture.sizeTag))
						picture.file.original = true;
					picture.file.name = name;
					fileService.saveFile(name, originalFile, picture.file);
				} else {
					picture.file = temp;
				}
			}

			pictureRepository.save(picture);
		}

		imageRepository.save(image);

		return image;
	}

	public Image createNewImage(String type, String name, java.io.File originalFile, String mime) throws Exception {

		ImageFile imageFile = imageUtil.getImageFile(originalFile);
		Image newImage = new Image(type);
		newImage.setTitle(name);
		newImage.setOriginalHash(imageFile.hash);

		Set<String> sizeTags = newImage.getSizeTags(); //this is all the sizes we need to upload
		Image existingImage = imageRepository.findOne(QImage.image.originalHash.eq(imageFile.hash));
		if (existingImage != null) {
			//if this image that we've found with the same original hash has all the needed sizes,
			//we can return this one because it matches what we need. Otherwise, upload the sizes we need
			if (existingImage.getSizeTags().stream().allMatch(sizeTags::contains)) {
				return existingImage;
			} else {
				newImage = existingImage;

				//this filters all values that already exist in the existing image and keep only the ones we need to upload
				sizeTags = sizeTags.stream().filter(s -> !existingImage.getSizeTags().contains(s)).collect(Collectors.toSet());
				newImage.setPictures(existingImage.getPictures());
			}
		} else {
			Picture originalPic = getOriginalPicture(mime, originalFile, imageFile);
			newImage.addPicture(originalPic);
		}

		for (String sizeTag : sizeTags) {
			Image.Size size = Image.Size.findByAbbr(sizeTag);
			Picture pic;
			if (size.xy != null) {
				pic = getPictureBySize(originalFile, size.toString(), size.xy);
			} else {
				pic = getPictureByQuality(originalFile, size.toString(), size.quality);
			}

			newImage.addPicture(pic);
		}

		newImage.setVertical(imageFile.vertical);

		return newImage;
	}

	private Picture getOriginalPicture(String mime, java.io.File originalFile, ImageFile imageFile) throws IOException {
		Picture originalPic = new Picture();
		originalPic.file = imageUtil.createNewImageTrixFile(mime, originalFile.length());
		originalPic.sizeTag = Image.SIZE_ORIGINAL;
		originalPic.file.name = originalFile.getName();

				File existingFile = fileRepository.findOne(QFile.file.hash.eq(imageFile.hash));
		if (existingFile != null) {
			originalPic.file = existingFile;
		}

		String hash = null;
		if (originalPic.file.id == null) {
			hash = amazonCloudService.uploadPublicImage(originalFile, originalFile.length(), null, originalPic.sizeTag, originalPic.file.getExtension(), false);
		}

		if (originalPic.file.hash == null || originalPic.file.hash.isEmpty()) {
			originalPic.file.hash = hash;
		}

		originalPic.height = imageFile.height;
		originalPic.width = imageFile.width;
		return originalPic;
	}

	Picture getPictureBySize(java.io.File originalFile, String sizeTag, Integer[] xy) throws Exception {
		Callable<ImageFile> callable = () -> imageUtil.resizeImage(originalFile, xy[0], xy[1], "png");

		return getPicture(callable, originalFile, sizeTag);
	}

	Picture getPictureByQuality(java.io.File originalFile, String sizeTag, Integer quality) throws Exception {
		Callable<ImageFile> callable = () -> imageUtil.resizeImage(originalFile, quality, "png", false, true);

		return getPicture(callable, originalFile, sizeTag);
	}

	private Picture getPicture(Callable<ImageFile> callable, java.io.File originalFile, String sizeTag) throws Exception {
		ImageFile imageFile;
		File existingFile;
		Picture pic = new Picture();
		pic.file = imageUtil.createNewImageTrixFile("image/png", originalFile.length());
		pic.sizeTag = sizeTag;

		imageFile = callable.call();

		existingFile = fileRepository.findOne(QFile.file.hash.eq(imageFile.hash).and(QFile.file.type.eq(File.EXTERNAL)));
		if (existingFile != null) {
			pic.file = existingFile;
		} else {
			amazonCloudService.uploadPublicImage(imageFile.file, imageFile.file.length(), imageFile.hash, pic.sizeTag, pic.file.getExtension(), false);
		}

		pic.file.size = imageFile.file.length();
		pic.file.hash = imageFile.hash;
		pic.height = imageFile.height;
		pic.width = imageFile.width;

		return pic;
	}
}
