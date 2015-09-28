package com.wordrails.business;

import com.wordrails.persistence.PostRepository;
import com.wordrails.test.AbstractTest;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;


public class PostServiceTest extends AbstractTest {

	@Autowired
	private PostService postService;
	@Autowired
	private PostRepository postRepository;

	@Test
	public void convertPostTest() {
		Integer postId = 1622;
		Post post = postRepository.findOne(postId);
		post.scheduledDate = new DateTime(new Date().getTime()).plusMinutes(1).toDate();
		postRepository.save(post);

		postService.convertPost(postId, "SCHEDULED");
	}
}
