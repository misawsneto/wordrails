package com.wordrails.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.api.PostView;
import com.wordrails.business.Post;
import com.wordrails.persistence.PostRepository;
import com.wordrails.util.WordrailsUtil;

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
		if (post.featuredImage != null) {
			postView.smallId = post.featuredImage.small.id;
			postView.mediumId = post.featuredImage.medium.id;
			postView.largeId = post.featuredImage.large.id;
			
			postView.imageId = post.imageId;
			postView.imageSmallId = post.imageSmallId;
			postView.imageMediumId = post.imageMediumId;
			postView.imageLargeId = post.imageLargeId;
		}
		postView.imageLandscape = post.imageLandscape;
		postView.date = post.date;
		postView.topper = post.topper;
		postView.state = post.state;
		postView.readsCount = post.readsCount;
		postView.favoritesCount = post.favoritesCount;
		postView.recommendsCount = post.recommendsCount;
		postView.commentsCount = post.commentsCount;
		postView.snippet = WordrailsUtil.simpleSnippet(post.body, 100);
		postView.authorName = post.author != null ? post.author.name : null;
		postView.authorUsername = post.author != null ? post.author.username : null;
		postView.authorCoverMediumId = post.author != null ? post.author.coverMediumId : null;
		postView.authorImageSmallId = post.author != null ? post.author.imageSmallId : null;
		postView.authorId = post.author != null ? post.author.id : null;
		postView.authorEmail = post.author != null ? post.author.email : null;
		postView.authorTwitter = post.author != null ? post.author.twitterHandle : null;
		postView.authorSmallImageId = post.author != null ? post.author.imageSmallId : null;
		postView.externalFeaturedImgUrl = post.externalFeaturedImgUrl;
		postView.externalVideoUrl = post.externalVideoUrl;
		postView.readTime = post.readTime;
		postView.lat = post.lat;
		postView.lng = post.lng;
		postView.imageCaptionText = post.imageCaptionText;
		postView.imageCreditsText = post.imageCreditsText;
		postView.imageTitleText = post.imageTitleText;
		postView.stationName = post.station.name;
		postView.stationId = post.station.id;
		
		return postView;
	}
}