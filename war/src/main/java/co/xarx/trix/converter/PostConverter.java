package co.xarx.trix.converter;

import co.xarx.trix.api.Category;
import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Term;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class PostConverter extends AbstractConverter<Post, PostView> {

	@Autowired
	PostRepository postRepository;
	
	@Override
	public Post convertFrom(PostView postView) {
		return postRepository.findOne(postView.id);
	}

	@Override
	public PostView convertTo(Post post) {
		PostView postView = new PostView();
		postView.id = post.id;
		postView.title = post.title;
		postView.subheading = post.subheading;
		postView.slug = post.slug;

		postView.tags = post.tags;
		if(post.terms != null && !post.terms.isEmpty()) {
			postView.categories = new HashSet<>();
			for (Term term : post.terms) {
				postView.categories.add(new Category(term.id, term.name));
			}
		}

		if (post.featuredImage != null) {
			postView.featuredImage = post.featuredImage.hashs;
			postView.imageSmallHash = post.featuredImage.hashs.get(Image.SIZE_SMALL);
			postView.imageMediumHash = post.featuredImage.hashs.get(Image.SIZE_MEDIUM);
			postView.imageLargeHash = post.featuredImage.hashs.get(Image.SIZE_LARGE);
		}

		postView.imageLandscape = post.imageLandscape;
		postView.date = post.date;
		postView.topper = post.topper;
		postView.readsCount = post.readsCount;
		postView.recommendsCount = post.recommendsCount;
		postView.commentsCount = post.commentsCount;
		postView.snippet = StringUtil.simpleSnippet(post.body);

		if (post.author != null) {
			postView.authorName = post.author.name;
			postView.authorUsername = post.author.username;

			if(post.author.cover != null) {
				postView.authorCoverMediumHash = post.author.coverMediumHash;
			}
			postView.authorImageSmallHash = post.author.imageSmallHash;


			postView.authorImageUrl = post.author.imageUrl;
			postView.authorCoverUrl = post.author.coverUrl;
			postView.authorId = post.author.id;
			postView.authorEmail = post.author.email;
			postView.authorTwitter = post.author.twitterHandle;
		}

		postView.externalFeaturedImgUrl = post.externalFeaturedImgUrl;
		postView.externalVideoUrl = post.externalVideoUrl;
		postView.readTime = post.readTime;
		postView.state = post.state;
		postView.scheduledDate = post.scheduledDate;
		postView.lat = post.lat;
		postView.lng = post.lng;
		postView.imageCaptionText = post.imageCaptionText;
		postView.imageCreditsText = post.imageCreditsText;
		postView.imageTitleText = post.imageTitleText;
		postView.stationName = post.station.name;
		postView.stationId = post.station.id;
		postView.notify = post.notify;
		
		return postView;
	}

	public PostView convertToView(Post post, boolean addBody) {
		PostView postView = convertTo(post);
		if(addBody)
			postView.body = post.body;
		return postView;
	}
}