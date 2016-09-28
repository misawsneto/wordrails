package co.xarx.trix.converter;

import co.xarx.trix.api.PostView;
import co.xarx.trix.api.TermView;
import co.xarx.trix.api.v2.CategoryData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Term;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
public class PostConverter extends AbstractConverter<Post, PostView> {

	PostRepository postRepository;
	TermConverter termConverter;

	@Autowired
	public PostConverter(PostRepository postRepository, TermConverter termConverter) {
		this.postRepository = postRepository;
		this.termConverter = termConverter == null ? new TermConverter() : termConverter;
	}

	@Override
	public Post convertFrom(PostView postView) {
		return postRepository.findOne(postView.postId);
	}

	@Override
	public PostView convertTo(Post post) {
		PostView postView = new PostView();
		postView.postId = post.id;
		postView.title = post.title;
		postView.subheading = post.subheading;
		postView.slug = post.slug;

		postView.tags = post.tags;
		if (post.terms != null && !post.terms.isEmpty()) {
			postView.categories = new HashSet<>();
			for (Term term : post.terms) {
				CategoryData category = new CategoryData(term.id, term.name);
				category.color = term.color;
				category.imageHash = term.getImageHash();
				postView.categories.add(category);
			}
		}

		if (post.getFeaturedImage() != null) {
			postView.featuredImageHash = post.getImageHash();
			postView.imageSmallHash = post.getImageSmallHash();
			postView.imageMediumHash = post.getImageMediumHash();
			postView.imageLargeHash = post.getImageLargeHash();
			postView.imageCredits = post.getFeaturedImage().getCredits();
		}

		postView.imageLandscape = post.imageLandscape;
		postView.date = post.date;
		postView.topper = post.topper;
		postView.recommendsCount = post.recommendsCount;
		postView.commentsCount = post.commentsCount;
		postView.snippet = StringUtil.simpleSnippet(post.body);

		postView.mediaImage = post.getMediaImage();

		postView.mediaAudio = post.getMediaAudio();

		postView.mediaVideo = post.getMediaVideo();

		postView.mediaGallery = post.getMediaGallery();

		if (post.author != null) {
			postView.authorName = post.author.name;
			postView.authorUsername = post.author.username;

			if (post.author.cover != null) {
				postView.authorCover = post.author.getCoverHash();
				postView.authorCoverMediumHash = post.author.getCoverMediumHash();
			}
			if (post.author.image != null) {
				postView.authorProfilePicture = post.author.getImageHash();
				postView.authorImageSmallHash = post.author.getImageSmallHash();
			}


//			postView.authorImageUrl = post.author.imageUrl;
//			postView.authorCoverUrl = post.author.coverUrl;
			postView.authorId = post.author.id;
			postView.authorEmail = post.author.email;
			postView.authorTwitter = post.author.twitterHandle;
		}

		postView.terms = getTermViews(post.terms);
		postView.externalVideoUrl = post.getFeaturedVideo() != null ? post.getFeaturedVideo().getExternalVideoUrl() : null;

		postView.featuredAudioHash = post.getFeaturedAudioHash();
		postView.featuredVideoHash = post.getFeaturedVideoHash();

//		Video video = post.getFeaturedVideo();
//		if(video == null) {
//			postView.video = new VideoDto();
//			postView.video.identifier = video.identifier;
//			postView.video.provider = video.identifier;
//			postView.video.id = video.id;
//		}

		postView.readTime = post.readTime;
		postView.state = post.state;
		postView.scheduledDate = post.scheduledDate;
		postView.unpublishDate = post.unpublishDate;
		postView.lat = post.lat;
		postView.lng = post.lng;

		postView.focusX = post.focusX;
		postView.focusY = post.focusY;

		postView.stationName = post.station.name;
		postView.stationId = post.station.id;
		postView.notify = post.notify;
		postView.tags = post.tags;

		postView.sponsored = post.sponsored;

		return postView;
	}

	public PostView convertToView(Post post, boolean addBody) {
		PostView postView = convertTo(post);
		if (addBody) postView.body = post.body;
		return postView;
	}

	public List<TermView> getTermViews(Collection<Term> terms) {
		if (terms != null && terms.size() > 0) return termConverter.convertToViews(new ArrayList<>(terms));
		return null;
	}
}