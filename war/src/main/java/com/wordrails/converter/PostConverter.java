package com.wordrails.converter;

import com.wordrails.api.PostView;
import com.wordrails.business.Post;
import com.wordrails.persistence.PostRepository;
import com.wordrails.util.WordrailsUtil;
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
		postView.snippet = WordrailsUtil.simpleSnippet(post.body, 100);
		postView.authorName = post.author != null ? post.author.name : null;
		postView.authorUsername = post.author != null ? post.author.username : null;
		postView.authorCoverMediumId = post.author != null ? post.author.coverMediumId : null;
		postView.authorImageSmallId = post.author != null ? post.author.imageSmallId : null;
		postView.authorSmallImageId = post.author != null ? post.author.imageSmallId : null;
		postView.authorImageSmallHash = post.author != null ? post.author.imageSmallHash : null;
		postView.authorCoverMediumHash = post.author != null ? post.author.coverMediumHash : null;
		postView.authorImageUrl = post.author != null ? post.author.imageUrl : null;
		postView.authorCoverUrl = post.author != null ? post.author.coverUrl : null;
		postView.authorId = post.author != null ? post.author.id : null;
		postView.authorEmail = post.author != null ? post.author.email : null;
		postView.authorTwitter = post.author != null ? post.author.twitterHandle : null;
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