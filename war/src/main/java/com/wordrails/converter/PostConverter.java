package com.wordrails.converter;

import com.wordrails.api.PostView;
import com.wordrails.business.Post;
import com.wordrails.persistence.PostRepository;
import com.wordrails.util.TrixUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PostConverter extends AbstractConverter<Post, PostView> {

	@Autowired PostRepository postRepository;
	
	@Override
	public Post convertToEntity(PostView postView) {
		return postRepository.findOne(postView.postId);
	}

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

		postView.imageSmallHash = postView.smallHash = post.imageSmallHash;
		postView.imageMediumHash = postView.mediumHash = post.imageMediumHash;
		postView.imageLargeHash = postView.largeHash = post.imageLargeHash;

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
			postView.authorSmallImageId = post.author.imageSmallId;
			postView.authorImageSmallHash = post.author.imageSmallHash;
			postView.authorCoverMediumHash = post.author.coverMediumHash;
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
		PostView postView = convertToView(post);
		if(addBody)
			postView.body = post.body;
		return postView;
	}
}