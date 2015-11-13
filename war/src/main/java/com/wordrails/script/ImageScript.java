package com.wordrails.script;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mysema.query.types.expr.BooleanExpression;
import com.wordrails.domain.File;
import com.wordrails.domain.Image;
import com.wordrails.domain.Picture;
import com.wordrails.domain.QFile;
import com.wordrails.persistence.FileRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
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

	@Async
	public void addPicturesToImages() {
		BooleanExpression b1 = QFile.file.name.eq("dsnias");
		BooleanExpression b2 = QFile.file.name.eq("dsnias");
		Iterable<File> fs = fileRepository.findAll(b1.and(b2));
		Set<Integer> fileIdsAlreadySaved = Sets.newHashSet();

		Map<Integer, File> files = Maps.newHashMap();
		for(File file : fs) {
			files.put(file.id, file);
		}

		System.out.println("Starting upload of files");
		for (File file : files.values()) {
			if(fileIdsAlreadySaved.contains(file.id) || file.getNetworkId() == null)
				continue;
			Set<Image> images = imageRepository.findByFileId(file.id);
			for(Image image : images) {
				if(image.hashs != null && !image.hashs.isEmpty()) continue;
				Map<String, String> hashs = Maps.newHashMap();
				Set<Picture> pictures = Sets.newHashSet();
				Set<String> type = Sets.newHashSet();
				type.addAll(Image.Type.findByAbbr(image.type).getSizeTags());
				type.add("original");
				for(String sizeTag : type) {
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

					Picture pic = new Picture(file.getNetworkId(), sizeTag, imageFile, image);
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
