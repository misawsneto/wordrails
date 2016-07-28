package co.xarx.trix.config.modelmapper;

import co.xarx.trix.api.v2.PostImageData;
import co.xarx.trix.domain.Image;
import org.modelmapper.PropertyMap;

public class PostImageDataMap extends PropertyMap<Image, PostImageData> {

	@Override
	protected void configure() {
		skip(destination.getTitle());
		skip(destination.getCredits());
		skip(destination.getCaption());
		skip(destination.isLandscape());
		using(new PictureSetToMapConverter()).map(source.getPictures(), destination.getPictures());
	}
}
