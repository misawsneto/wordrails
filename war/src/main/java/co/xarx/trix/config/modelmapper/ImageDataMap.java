package co.xarx.trix.config.modelmapper;

import co.xarx.trix.api.v2.ImageData;
import co.xarx.trix.domain.Image;
import org.modelmapper.PropertyMap;

public class ImageDataMap extends PropertyMap<Image, ImageData> {

	@Override
	protected void configure() {
		using(new PictureSetToMapConverter()).map(source.getPictures(), destination.getPictures());
	}
}
