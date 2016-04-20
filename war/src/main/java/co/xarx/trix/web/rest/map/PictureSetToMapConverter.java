package co.xarx.trix.web.rest.map;

import co.xarx.trix.api.v2.PictureData;
import co.xarx.trix.domain.Picture;
import org.modelmapper.AbstractConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
