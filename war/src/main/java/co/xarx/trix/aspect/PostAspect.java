package co.xarx.trix.aspect;

import co.xarx.trix.domain.ESPost;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.ESPostRepository;
import co.xarx.trix.services.ElasticSearchService;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class PostAspect {

	@Autowired
	private ESPostRepository esPostRepository;
	@Autowired
	private ElasticSearchService elasticSearchService;

	@AfterReturning("within(co.xarx.trix.persistence.PostRepository+) && execution(* *..save(*)) && args(post)")
	public void savePost(Post post) throws Throwable {
		if (post.state.equals(Post.STATE_PUBLISHED)) {
			elasticSearchService.mapThenSave(post, ESPost.class);
		} else {
			esPostRepository.delete(post.getId());
		}
	}
}
