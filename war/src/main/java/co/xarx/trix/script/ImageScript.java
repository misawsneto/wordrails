package co.xarx.trix.script;


import co.xarx.trix.domain.File;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Picture;
import co.xarx.trix.persistence.FileRepository;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PictureRepository;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class ImageScript {

	@Autowired
	private FileRepository fileRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private PictureRepository pictureRepository;

	public void addPicturesToImages() {
		Iterable<File> fs = fileRepository.findAll();
		Set<Integer> fileIdsAlreadySaved = Sets.newHashSet();

		Map<Integer, File> files = Maps.newHashMap();
		for (File file : fs) {
			files.put(file.id, file);
		}

		System.out.println("Starting upload of files");
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
					pictureRepository.save(pic);
					pictures.add(pic);
					hashs.put(sizeTag, imageFile.hash);
					fileIdsAlreadySaved.add(imageFile.id);
				}

				System.out.println("saving image " + image.id + " type " + image.type);
				image.pictures = pictures;
				image.hashs = hashs;
				imageRepository.save(image);
			}
		}

		System.out.println("Done");
	}
}
