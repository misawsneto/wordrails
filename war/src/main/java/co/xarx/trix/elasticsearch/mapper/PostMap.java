package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.ESPost;
import org.modelmapper.PropertyMap;

public class PostMap<T extends Post> extends PropertyMap<T, ESPost> {

	@Override
	protected void configure() {
		map().setStationId(source.getStation().id);
		map().setFeaturedImageHash(source.getFeaturedImage().getOriginalHash());
		map().setFeaturedImageCaption(source.getFeaturedImage().getCaption());
		map().setFeaturedImageCredits(source.getFeaturedImage().getCredits());
		map().setFeaturedImageTitle(source.getFeaturedImage().getTitle());
	}
}
