package co.xarx.trix.web.rest.map;

import co.xarx.trix.api.v2.CategoryData;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Term;
import org.modelmapper.AbstractConverter;
import org.modelmapper.PropertyMap;

import java.util.Set;
import java.util.stream.Collectors;

public class PostDataMap extends PropertyMap<Post, PostData> {

	class TermToIntegerConverter extends AbstractConverter<Set<Term>, Set<Integer>> {

		@Override
		protected Set<Integer> convert(Set<Term> terms) {
			return terms.stream().map(Term::getId).collect(Collectors.toSet());
		}
	}

	class TermToCategoryConverter extends AbstractConverter<Set<Term>, Set<CategoryData>> {

		@Override
		protected Set<CategoryData> convert(Set<Term> terms) {
			return terms.stream()
					.map(term -> new CategoryData(term.getId(), term.getName()))
					.collect(Collectors.toSet());
		}
	}


	@Override
	protected void configure() {
		map().setImageTitle(source.getImageTitleText());
		map().setImageCredits(source.getImageCreditsText());
		map().setImageCaption(source.getImageCaptionText());
		map().setSnippet(null);
		map().setNotified(source.isNotify());
		using(new TermToIntegerConverter()).map(source.getTerms(), destination.getCategoriesIds());
		using(new TermToCategoryConverter()).map(source.getTerms(), destination.getCategories());

		map().setAuthor(null);
	}
}
