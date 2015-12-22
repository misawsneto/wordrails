package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.domain.Post;
import co.xarx.trix.elasticsearch.domain.ESPost;
import org.modelmapper.PropertyMap;

public class PostMap<T extends Post> extends PropertyMap<T, ESPost> {

	@Override
	protected void configure() {
		map().setStationId(source.getStation().id);
		map(source.getTerms(), destination.getCategories());
		map().setSponsored(source.sponsor != null);
		map().setFeaturedImage(source.getFeaturedImage().hashs);
		map().setFeaturedImageCaption(source.getFeaturedImage().caption);
		map().setFeaturedImageCredits(source.getFeaturedImage().credits);
		map().setFeaturedImageTitle(source.getFeaturedImage().title);
	}
}
