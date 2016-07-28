package co.xarx.trix.services.post;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Slf4j
@Service
@IntegrationTestBean
public class PostModerationService {

	private PostRepository postRepository;

	@Autowired
	public PostModerationService(PostRepository postRepository) {
		this.postRepository = postRepository;
	}

	public void publish(Integer postId) throws IllegalStateException {
		Assert.notNull(postId);
		Assert.isTrue(postId > 0, "Post ID must be a positive int");
		String state = postRepository.findStateById(postId);

		if (state == null)
			throw new IllegalArgumentException("Post with id " + postId + " does not exist");

		if (state.equals(Post.STATE_PUBLISHED))
			throw new IllegalStateException("Post is already published");

		postRepository.updateState(postId, Constants.Post.STATE_PUBLISHED);
	}

	public void unpublish(Integer postId) throws IllegalStateException {
		Assert.notNull(postId);
		Assert.isTrue(postId > 0, "Post ID must be a positive int");
		String state = postRepository.findStateById(postId);

		if (state == null)
			throw new IllegalArgumentException("Post with id " + postId + " does not exist");

		if (!state.equals(Post.STATE_PUBLISHED))
			throw new IllegalStateException("Post is not published");

		postRepository.updateState(postId, Constants.Post.STATE_UNPUBLISHED);
	}
}
