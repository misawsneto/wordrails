package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.ESPost;
import org.modelmapper.PropertyMap;

public class PostViewMap extends PropertyMap<ESPost, PostView> {

	@Override
	protected void configure() {
		map().setAuthorId(source.getAuthorId());
	}
}
