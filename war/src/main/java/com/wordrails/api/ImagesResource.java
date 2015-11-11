package com.wordrails.api;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.google.common.collect.Sets;
import com.wordrails.business.File;
import com.wordrails.business.Image;
import com.wordrails.business.Picture;
import com.wordrails.persistence.FileRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.PictureRepository;
import com.wordrails.services.AmazonCloudService;
import com.wordrails.util.FileUtil;
import com.wordrails.util.FilesUtil;
import com.wordrails.util.ImageUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

@Path("/images")
@Consumes(MediaType.WILDCARD)
@Component
public class ImagesResource {

	private static final Integer MAX_SIZE = 6291456;

	Logger log = Logger.getLogger(ImagesResource.class.getName());

	@Autowired
	private AmazonCloudService amazonCloudService;
	@Autowired
	private PictureRepository pictureRepository;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private ImageRepository imageRepository;


	@POST
	@Path("/upload")
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces(MediaType.APPLICATION_JSON)
	public Response uploadImage(@QueryParam("imageType") String type, @Context HttpServletRequest request) throws FileUploadException, IOException {
		FileItem item = FileUtil.getFileFromRequest(request);

		if (item == null) {
			return Response.noContent().build();
		} else if (!validate(item)) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}

		Image newImage = new Image();
		newImage.type = type;

		Set<Picture> pictures = Sets.newHashSet();
		String hash = FilesUtil.getHash(item.getInputStream());
		Integer networkId = (Integer) request.getSession().getAttribute("networkId");
		String networkSubdomain = (String) request.getSession().getAttribute("networkSubdomain");

		Image existingImage = null;
		Set<Image> images = imageRepository.findByFileHashFetchPictures(hash);
		if (images != null && !images.isEmpty()) {
			for(Image i : images) {
				if(i.type.equals(type)) {
					existingImage = i;
				}
			}
		}
		if (existingImage != null && existingImage.pictures != null && !existingImage.pictures.isEmpty()) {
			for (Picture pic : existingImage.pictures) {
				if (!amazonCloudService.exists(networkSubdomain, pic.file.hash)) { //if image doesnt exist on amazon servers, upload it. it should exist
					java.io.File tmpFile = FilesUtil.createNewTempFile(item.getInputStream());
					try {
						amazonCloudService.uploadPublicImage(tmpFile, pic.file.size,
								networkSubdomain, pic.file.hash, pic.sizeTag, pic.file.getExtension(), true);
					} catch (AmazonS3Exception e) {
						throw new FileUploadException("Error uploading image to s3", e);
					}
				}

				pictures.add(pic);
			}
		} else {
			Image.Type imageType = Image.Type.findByAbbr(type);
			if (imageType == null) {
				throw new BadRequestException("Image type is not valid");
			}


			Picture originalPic = new Picture();
			originalPic.file = ImageUtil.createNewImageTrixFile(networkId, item.getContentType(), item.getSize());
			originalPic.sizeTag = Image.SIZE_ORIGINAL;
			originalPic.networkId = networkId;
			java.io.File originalFile = FilesUtil.createNewTempFile(item.getInputStream());
			ImageUtil.ImageFile imageFile = ImageUtil.getMeasuredImageFile(originalFile);

			File existingFile = fileRepository.findByHashAndNetworkId(imageFile.hash, networkId);
			if(existingFile != null) {
				originalPic.file = existingFile;
			} else {
				hash = amazonCloudService.uploadPublicImage(originalFile, originalFile.length(), networkSubdomain, null, originalPic.sizeTag, originalPic.file.getExtension(), false);
			}

			originalPic.file.hash = hash;
			originalPic.height = imageFile.height;
			originalPic.width = imageFile.width;
			pictures.add(originalPic);
			fileRepository.save(originalPic.file);

			if (imageType.qualities != null) {
				Map<String, Integer> qualities = imageType.qualities;
				for (Map.Entry<String, Integer> entry : qualities.entrySet()) {
					Picture pic = new Picture();
					pic.file = ImageUtil.createNewImageTrixFile(networkId, item.getContentType(), item.getSize());
					pic.sizeTag = entry.getKey();
					pic.networkId = networkId;

					Integer quality = entry.getValue();
					imageFile = ImageUtil.resizeImage(originalFile, quality, pic.file.getExtension(), false, true);

					existingFile = fileRepository.findByHashAndNetworkId(imageFile.hash, networkId);
					if(existingFile != null) {
						pic.file = existingFile;
					} else {
						hash = amazonCloudService.uploadPublicImage(imageFile.file,
								imageFile.file.length(), networkSubdomain, imageFile.hash, pic.sizeTag, pic.file.getExtension(), false);
					}

					pic.file.size = imageFile.file.length();
					pic.file.hash = imageFile.hash;
					pic.height = imageFile.height;
					pic.width = imageFile.width;
					pictures.add(pic);
					fileRepository.save(pic.file);
				}
			} else if (imageType.sizes != null) {
				Map<String, Integer[]> sizes = imageType.sizes;
				for (Map.Entry<String, Integer[]> entry : sizes.entrySet()) {
					Picture pic = new Picture();
					pic.file = ImageUtil.createNewImageTrixFile(networkId, item.getContentType(), item.getSize());
					pic.sizeTag = entry.getKey();
					pic.networkId = networkId;

					Integer[] xy = entry.getValue();
					imageFile = ImageUtil.resizeImage(originalFile, xy[0], xy[1], pic.file.getExtension());

					existingFile = fileRepository.findByHashAndNetworkId(imageFile.hash, networkId);
					if(existingFile != null) {
						pic.file = existingFile;
					} else {
						hash = amazonCloudService.uploadPublicImage(imageFile.file,
								imageFile.file.length(), networkSubdomain, imageFile.hash, pic.sizeTag, pic.file.getExtension(), false);
					}

					pic.file.size = imageFile.file.length();
					pic.file.hash = imageFile.hash;
					pic.height = imageFile.height;
					pic.width = imageFile.width;
					pictures.add(pic);
					fileRepository.save(pic.file);
				}
			}

			newImage.pictures = pictures;

			if(originalFile.exists())
				originalFile.delete();
		}

		for(Picture pic : pictures) {
			pictureRepository.save(pic);
		}

		newImage.original = newImage.getPicture(Image.SIZE_ORIGINAL).file;
		Picture largePicture = newImage.getPicture(Image.SIZE_LARGE);
		Picture mediumPicture = newImage.getPicture(Image.SIZE_MEDIUM);
		Picture smallPicture = newImage.getPicture(Image.SIZE_MEDIUM);

		if(largePicture == null) {
			newImage.large = newImage.original;
		} else {
			newImage.large = largePicture.file;
		}

		if(mediumPicture == null) {
			newImage.medium = newImage.large;
		} else {
			newImage.medium = mediumPicture.file;
		}

		if(smallPicture == null) {
			newImage.small = newImage.medium;
		} else {
			newImage.small = smallPicture.file;
		}

		imageRepository.save(newImage);

		return Response.ok().entity("{\"hash\":\"" + hash + "\", \"imageId\":" + newImage.id + "}").build();
	}

	private boolean validate(FileItem item) throws FileUploadException {
		if (item.getFieldName().equals("contents") || item.getFieldName().equals("file")) {
			if (item.getSize() > MAX_SIZE) {
				throw new FileUploadException("Maximum file size is 6MB");
			}

			return true;
		}
		return false;
	}


}