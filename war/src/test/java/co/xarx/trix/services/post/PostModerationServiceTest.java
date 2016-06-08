package co.xarx.trix.services.post;

import co.xarx.trix.IntegrationTest;
import co.xarx.trix.TestArtifactsFactory;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.StationRepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
public class PostModerationServiceTest extends IntegrationTest {

	@Autowired
	PostRepository postRepository;
	@Autowired
	StationRepository stationRepository;
	@Autowired
	PersonRepository personRepository;
	@Autowired
	PostModerationService postModerationService;

	@Ignore
	@Test(expected = IllegalArgumentException.class)
	public void throwExceptionForNotExistentPost() throws Exception {
		postModerationService.publish(10);
	}

	@Ignore
	@Test(expected = AccessDeniedException.class)
	public void accessDeniedForPublishingWithAnonymousUser() throws Exception {
		Post post = TestArtifactsFactory.createPost();
		post.setFeaturedImage(null);
		post.setState(Post.STATE_PUBLISHED);
		stationRepository.save(post.station);
		personRepository.save(post.author);
		postRepository.save(post);

		postModerationService.publish(post.getId());
	}

}