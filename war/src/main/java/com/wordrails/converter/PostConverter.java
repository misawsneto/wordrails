package com.wordrails.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.wordrails.api.PostView;
import com.wordrails.api.PostsResource;
import com.wordrails.business.Post;
import com.wordrails.persistence.PostRepository;

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
		if (post.featuredImage != null) {
			postView.smallId = post.featuredImage.small.id;
			postView.mediumId = post.featuredImage.medium.id;
			postView.largeId = post.featuredImage.large.id;
		}
		postView.date = post.date;
		postView.topper = post.topper;
		postView.state = post.state;
		postView.snippet = PostsResource.simpleSnippet(post.body);
		return postView;
	}
}