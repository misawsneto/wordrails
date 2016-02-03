package co.xarx.trix.aspect;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.QStationRole;
import co.xarx.trix.domain.Station;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.elasticsearch.repository.ESPostRepository;
import co.xarx.trix.persistence.StationRolesRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.services.SchedulerService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.NotAuthorizedException;

@Aspect
@Component
public class PostAspect {

	@Autowired
	private ESPostRepository esPostRepository;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private StationRolesRepository rolesRepository;
	@Autowired
	private TrixAuthenticationProvider authenticationProvider;

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
			//post = findOne(post.getId()); //do it again so modelmapper don't cry... stupid framework
			elasticSearchService.saveIndex(post, ESPost.class, esPostRepository);
		} else {
			elasticSearchService.deleteIndex(post.getId(), esPostRepository);

		}
	}

	@Around("execution(* co.xarx.trix.persistence.PostRepository+.findBySlug(..))")
	public Object checkPermission(ProceedingJoinPoint pjp) throws Throwable {
		Post post = (Post) pjp.proceed();

		Person person = authenticationProvider.getLoggedPerson();

		QStationRole sr = QStationRole.stationRole;
		if(post != null && !post.station.visibility.equals(Station.UNRESTRICTED) &&
				rolesRepository.findOne(sr.person.id.eq(person.id).and(sr.station.id.eq(post.station.id))) == null) {
			throw new NotAuthorizedException("User does not have permission to this station");
		}

		return post;
	}
}
