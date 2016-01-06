package co.xarx.trix.script;


import co.xarx.trix.aspect.annotations.IgnoreMultitenancy;
import co.xarx.trix.aspect.annotations.TimeIt;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.ImageService;
import co.xarx.trix.util.FileUtil;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.rometools.utils.Strings;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

@Component
public class ImageScript {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private ImageService imageService;
	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private PictureRepository pictureRepository;
	@Autowired
	private FileContentsRepository fileContentsRepository;

	@Autowired
	private QueryPersistence queryPersistence;

	public void mergeRepeatedImages(){
		List<Object[]> repeated = queryPersistence.getRepeatedImage();
		List<Integer> removeImageList = new ArrayList<>();

		for(Object[] image: repeated){
			Integer imageId = (Integer) image[0];
			String imageHash = (String) image[1];

			//Fix post: featuredImage_id reference
			List<Integer> repeatedImagesIds = queryPersistence.getRepeatedImageHash(imageHash);

			if(repeatedImagesIds.contains(imageId)){
				repeatedImagesIds.remove(imageId);
			}

			queryPersistence.updatePostFeaturedImageId(repeatedImagesIds, imageId);
//			queryPersistence.updateNetworkLogoId(repeatedImagesIds, imageId);
			queryPersistence.updateNetworkLoginImageId(repeatedImagesIds, imageId);
			queryPersistence.updateNetworkFavicon(repeatedImagesIds);
//			queryPersistence.updateNetworkFaviconId(repeatedImagesIds, imageId);
			queryPersistence.updateNetworkSplashImageId(repeatedImagesIds, imageId);
			queryPersistence.updateStationLogo(repeatedImagesIds, imageId);
//			queryPersistence.updateImagePicture(repeatedImagesIds, imageId);

			removeImageList.addAll(repeatedImagesIds);
		}
		queryPersistence.deleteImagePicture(removeImageList);
		queryPersistence.deleteImageHash(removeImageList);
		queryPersistence.deleteImageList(removeImageList);
	}

	@TimeIt
	@IgnoreMultitenancy
	public void addPicturesToImages() {
		Map<Image, String> networksImages = new HashMap<>();
		for (Network network : networkRepository.findAll()) {
			if (network.loginImage != null) networksImages.put(network.loginImage, network.getTenantId());
			if (network.splashImage != null) networksImages.put(network.splashImage, network.getTenantId());
			if (network.favicon != null) networksImages.put(network.favicon, network.getTenantId());
		}
		for (Station station : stationRepository.findAll(QStation.station.logo.isNotNull())) {
			if (station.logo != null) networksImages.put(station.logo, station.getTenantId());
		}

		Set<Integer> fileIdsAlreadySaved = new HashSet();
		for (Image img : networksImages.keySet()) {
			String tenantId = networksImages.get(img);
			File original = img.original;
			original.setTenantId(tenantId);
			File large = img.large;
			large.setTenantId(tenantId);
			File medium = img.medium;
			medium.setTenantId(tenantId);
			File small = img.small;
			small.setTenantId(tenantId);

			uploadIfInternal(original, tenantId);
			fileIdsAlreadySaved.add(original.getId());

			if (fileIdsAlreadySaved.add(large.getId())) {
				uploadIfInternal(large, tenantId);
			}

			if (fileIdsAlreadySaved.add(medium.getId())) {
				uploadIfInternal(medium, tenantId);
			}

			if (fileIdsAlreadySaved.add(small.getId())) {
				uploadIfInternal(small, tenantId);
			}
		}

		Iterable<File> fs = fileRepository.findAll(QFile.file.tenantId.isNotEmpty());

		Map<Integer, File> files = new HashMap<>();
		for (File file : fs) {
			files.put(file.id, file);
		}

		int imageCount = 0;
		fileIdsAlreadySaved.clear();

		log.debug("Starting upload of files");
		for (File file : files.values()) {
			if (fileIdsAlreadySaved.contains(file.id) || Strings.isEmpty(file.getTenantId())) continue;
			Set<Image> images = imageRepository.findByFileId(file.id);
			for (Image image : images) {
				if (image.hashs == null || image.hashs.isEmpty()) {
					Map<String, String> hashs = Maps.newHashMap();
					Set<Picture> pictures = Sets.newHashSet();
					Set<String> type = Sets.newHashSet();
					type.addAll(Image.Type.findByAbbr(image.type).getSizeTags());
					type.add("original");
					for (String sizeTag : type) {
						File imageFile;
						switch (sizeTag) {
							case "small":
								imageFile = image.small; //files.get(image.small.id);
								break;
							case "medium":
								imageFile = image.medium; //files.get(image.medium.id);
								break;
							case "large":
								imageFile = image.large; //files.get(image.large.id);
								break;
							case "original":
								imageFile = image.original; //files.get(image.large.id);
								break;
							default:
								continue;
						}

						Picture pic = new Picture(sizeTag, imageFile);
						pic.setTenantId(file.getTenantId());
						pictureRepository.save(pic);
						pictures.add(pic);
						hashs.put(sizeTag, imageFile.hash);
						fileIdsAlreadySaved.add(imageFile.id);
					}
					image.pictures = pictures;
					image.hashs = hashs;
					imageCount++;
				}

				image.setTenantId(file.getTenantId());
				imageRepository.save(image);
			}
		}

		log.debug("Picture script done. imageCount: " + imageCount);
	}

	public void uploadIfInternal(File file, String tenantId) {
		FileContents f = null;
		if(file.type.equals(File.INTERNAL)) {
			f = fileContentsRepository.findOne(file.getId());
		}

		if (file.type.equals(File.INTERNAL) && f.contents != null) {
			try {
				java.io.File tmpFile = FileUtil.createNewTempFile(f.contents.getBinaryStream());
				imageService.uploadIfDoesntExist(tmpFile, f.hash, f.size, "original", f.getExtension());
				f.type = File.EXTERNAL;
				f.contents = null;
				fileContentsRepository.save(f);
			} catch (IOException | SQLException | FileUploadException e) {
				e.printStackTrace();
			}
		}

		file.setTenantId(tenantId);
		fileRepository.save(file);
		log.debug("saving file " + file.id + " tenant " + tenantId);
	}
}
