package co.xarx.trix.web.rest.mapper;

import co.xarx.trix.api.v2.PictureData;
import co.xarx.trix.domain.Picture;
import org.modelmapper.PropertyMap;

public class PictureDataMap extends PropertyMap<Picture, PictureData> {

	@Override
	protected void configure() {
		map(source.getFile().getHash(), destination.getHash());
		map().setUrl(null); //TODO: add url
	}
}
