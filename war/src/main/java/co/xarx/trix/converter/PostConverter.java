package co.xarx.trix.converter;

import co.xarx.trix.api.TermView;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Term;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.api.PostView;
import co.xarx.trix.util.TrixUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class PostConverter extends AbstractConverter<Post, PostView> {

	@Autowired
	PostRepository postRepository;

	@Override
	public Post convertToEntity(PostView postView) {
		return postRepository.findOne(postView.postId);
	}

    @Autowired
    TermConverter termConverter;

	@Override
	public PostView convertToView(Post post) {
		PostView postView = new PostView();
		postView.postId = post.id;
		postView.title = post.title;
		postView.subheading = post.subheading;
		postView.slug = post.slug;

		postView.smallId = post.imageSmallId;
		postView.mediumId = post.imageMediumId;
		postView.largeId = post.imageLargeId;

		if (post.featuredImage != null) {
			postView.featuredImage = post.featuredImage.hashs;
			postView.imageSmallHash = post.featuredImage.hashs.get(Image.SIZE_SMALL);
			postView.imageMediumHash = post.featuredImage.hashs.get(Image.SIZE_MEDIUM);
			postView.imageLargeHash = post.featuredImage.hashs.get(Image.SIZE_LARGE);
			if (post.featuredImage != null && post.featuredImage.hashs != null && post.featuredImage.hashs.size() > 0) {
				postView.imageSmallHash = post.imageSmallHash;
				postView.imageMediumHash = post.imageMediumHash;
				postView.imageLargeHash = post.imageLargeHash;
			}
		}

		postView.imageId = post.imageId;
		postView.imageSmallId = post.imageSmallId;
		postView.imageMediumId = post.imageMediumId;
		postView.imageLargeId = post.imageLargeId;
		
		postView.imageLandscape = post.imageLandscape;
		postView.date = post.date;
		postView.topper = post.topper;
		postView.readsCount = post.readsCount;
		postView.recommendsCount = post.recommendsCount;
		postView.commentsCount = post.commentsCount;
		postView.snippet = TrixUtil.simpleSnippet(post.body, 100);

		if (post.author != null) {
			postView.authorName = post.author.name;
			postView.authorUsername = post.author.username;
			postView.authorCoverMediumId = post.author.coverMediumId;
			postView.authorImageSmallId = post.author.imageSmallId;
			postView.authorSmallImageId = post.author.imageSmallId; //is this being used?

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

        postView.terms = getTermViews(post.terms);
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
        postView.tags = post.tags;
		
		return postView;
	}

	public PostView convertToView(Post post, boolean addBody) {
		PostView postView = convertToView(post);
		if(addBody)
			postView.body = post.body;
		return postView;
	}

    public List<TermView> getTermViews(Collection<Term> terms){
        if(terms != null && terms.size() > 0)
            return termConverter.convertToViews(new ArrayList<>(terms));
        return  null;
    }
}