package co.xarx.trix.aspect;

import co.xarx.trix.domain.Post;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.elasticsearch.repository.ESPostRepository;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.services.SchedulerService;
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
	private SchedulerService schedulerService;
	@Autowired
	private ElasticSearchService elasticSearchService;

	@AfterReturning("within(co.xarx.trix.persistence.PostRepository+) && execution(* *..save(*)) && args(post)")
	public void checkMultitenantEntity(Post post) throws Throwable {
		if (post.state.equals(Post.STATE_SCHEDULED)) {
				schedulerService.schedule(post.getId(), post.scheduledDate);
		} else {
			if (post.scheduledDate != null) { //if converting FROM scheduled, unschedule
				schedulerService.unschedule(post.getId());
			}
		}

		if (post.state.equals(Post.STATE_PUBLISHED)) {
			//				post = findOne(post.getId()); //do it again so modelmapper don't cry... stupid framework
			elasticSearchService.saveIndex(post, ESPost.class, esPostRepository);
		} else {
			elasticSearchService.deleteIndex(post.getId(), esPostRepository);

		}
	}
}
