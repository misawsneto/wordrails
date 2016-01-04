package co.xarx.trix.script;


import co.xarx.trix.domain.File;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Picture;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PictureRepository;
import co.xarx.trix.persistence.QueryPersistence;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class ImageScript {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private PictureRepository pictureRepository;

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
			queryPersistence.updateNetworkLogoId(repeatedImagesIds, imageId);
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

	public void addPicturesToImages() {
		Iterable<File> fs = fileRepository.findAll();
		Set<Integer> fileIdsAlreadySaved = Sets.newHashSet();

		Map<Integer, File> files = Maps.newHashMap();
		for (File file : fs) {
			files.put(file.id, file);
		}

		int imageCount = 0;

		log.debug("Starting upload of files");
		for (File file : files.values()) {
			if (fileIdsAlreadySaved.contains(file.id) || file.getNetworkId() == null) continue;
			Set<Image> images = imageRepository.findByFileId(file.id);
			for (Image image : images) {
				Map<String, String> hashs = Maps.newHashMap();

				if (image.hashs != null && !image.hashs.isEmpty()) continue;

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
					pic.setNetworkId(image.getNetworkId());
					pic.setTenantId(image.getTenantId());
					pictureRepository.save(pic);
					pictures.add(pic);
					hashs.put(sizeTag, imageFile.hash);
					fileIdsAlreadySaved.add(imageFile.id);
				}

				imageCount++;
//				System.out.println("saving image " + image.id + " type " + image.type);
				image.pictures = pictures;
				image.hashs = hashs;
				imageRepository.save(image);
			}
		}

		log.debug("Picture script done. imageCount: " + imageCount);
	}
}
