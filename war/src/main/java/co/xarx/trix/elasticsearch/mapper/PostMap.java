package co.xarx.trix.elasticsearch.mapper;

import co.xarx.trix.domain.ESPost;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Term;
import org.modelmapper.AbstractConverter;
import org.modelmapper.PropertyMap;

import java.util.Set;
import java.util.stream.Collectors;

public class PostMap<T extends Post> extends PropertyMap<T, ESPost> {

	class TermToIntegerConverter extends AbstractConverter<Set<Term>, Set<Integer>> {

		@Override
		protected Set<Integer> convert(Set<Term> terms) {
			if(terms != null && terms.size() > 0)
				return terms.stream().map(Term::getId).collect(Collectors.toSet());
			else
				return null;
		}
	}


	@Override
	protected void configure() {
		map().setStationId(source.getStation().id);
		map().setFeaturedImageHash(source.getFeaturedImage().getOriginalHash());
		map().setFeaturedImageCaption(source.getFeaturedImage().getCaption());
		map().setFeaturedImageCredits(source.getFeaturedImage().getCredits());
		map().setFeaturedImageTitle(source.getFeaturedImage().getTitle());
		using(new TermToIntegerConverter()).map(source.getTerms(), destination.categories);
	}
}
