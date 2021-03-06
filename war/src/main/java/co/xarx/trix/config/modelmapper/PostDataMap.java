package co.xarx.trix.config.modelmapper;

import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Term;
import co.xarx.trix.elasticsearch.mapper.PostMap;
import org.modelmapper.AbstractConverter;
import org.modelmapper.PropertyMap;

import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

public class PostDataMap extends PropertyMap<Post, PostData> {

	private class TermToIntegerConverter extends AbstractConverter<Set<Term>, Set<Integer>> {

		@Override
		protected Set<Integer> convert(Set<Term> terms) {
			return terms.stream().map(Term::getId).collect(Collectors.toSet());
		}
	}

	class ScheduledDateCondition extends AbstractConverter<Date, Date> {

		@Override
		protected Date convert(Date source) {
			return source != null ? source : new Date(0);
		}
	}

//	class UnpublishDateCondition extends AbstractConverter<Date, Date> {
//
//		@Override
//		protected Date convert(Date source) {
//			return source != null ? source : new Date(Long.MAX_VALUE);
//		}
//	}


	@Override
	protected void configure() {
		map(source.getBody(), destination.getBody());
		map(source.getFeaturedImage(), destination.getImage());
		map(source.getFeaturedImage().getOriginalHash(), destination.getImageHash());
		map(source.getImageCredits(), destination.getImage().getCredits());
		map(source.isImageLandscape(), destination.getImage().isLandscape());
		map(source.getStationId(), destination.getStationId());
		map(source.getAuthor().getName(), destination.getAuthorName());
		map(source.getAuthor().getImageHash(), destination.getAuthorImageHash());
		map(source.getAuthor().getCoverHash(), destination.getAuthorCoverHash());

		map().setNotified(source.isNotify());
		using(new TermToIntegerConverter()).map(source.getTerms(), destination.getCategoriesIds());
		map(source.getTerms(), destination.getCategories());
		map(source.getFeaturedVideo().getExternalVideoUrl(), destination.getVideo().getUrl());
		map(source.getUnpublishDate(), destination.getUnpublishDate());

		using(new PostDataMap.ScheduledDateCondition()).map(source.getScheduledDate(), destination.getScheduledDate());

//		Condition<Image, PostData> picsNull = c -> c.getSource().getPictures() == null;

		skip(destination.getSnippet());
		skip(destination.getReadTime());
//		skip(destination.getAuthor());
//		when(picsNull).skip(source.getFeaturedImage(), destination.getImage());
//		skip(destination.getVideo());
//		skip(destination.getAudio());
//		skip(destination.getCategories());
	}
}
