package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.ImageData;
import co.xarx.trix.api.v2.PictureData;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Picture;
import org.modelmapper.AbstractConverter;
import org.modelmapper.PropertyMap;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ImageDataMap extends PropertyMap<Image, ImageData> {

	class PictureSetToMapConverter extends AbstractConverter<Set<Picture>, Map<String, PictureData>> {

		@Override
		protected Map<String, PictureData> convert(Set<Picture> pictures) {
			Map<String, PictureData> picturesMap = new HashMap<>();
			for (Picture picture : pictures) {
				PictureData data = new PictureData();
				data.setUrl(null); //TODO: add url
				data.setHash(picture.getFile().getHash());
				picturesMap.put(picture.getSizeTag(), data);
			}

			return picturesMap;
		}
	}

	@Override
	protected void configure() {
		using(new PictureSetToMapConverter()).map(source.getPictures(), destination.getPictures());
	}
}
