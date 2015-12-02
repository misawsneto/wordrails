package co.xarx.trix.services;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.PostReadRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.persistence.elasticsearch.PostEsRepository;
import co.xarx.trix.scheduler.jobs.PostScheduleJob;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import org.hibernate.exception.ConstraintViolationException;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class PostService {

	private static final Logger log = LoggerFactory.getLogger(PostService.class);

	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private Scheduler scheduler;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private PostReadRepository postReadRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private PostEsRepository postEsRepository;

	public void removePostIndex(Post post) {
		postEsRepository.delete(post);
	}

	public void updatePostIndex(Post post) {
		postEsRepository.update(post);
	}

	public Post convertPost(int postId, String state) {
		Post dbPost = postRepository.findOne(postId);

		if (dbPost != null) {
			log.debug("Before convert: " + dbPost.getClass().getSimpleName());
			if (state.equals(dbPost.state)) {
				return dbPost; //they are the same type. no need for convertion
			}

			if (dbPost.state.equals(Post.STATE_SCHEDULED)) { //if converting FROM scheduled, unschedule
				unschedule(dbPost.id);
			} else if (state.equals(Post.STATE_SCHEDULED)) { //if converting TO scheduled, schedule
				schedule(dbPost.id, dbPost.scheduledDate);
			}

			dbPost.state = state;

			queryPersistence.changePostState(postId, state);
			//removePostIndex(dbPost);
			updatePostIndex(dbPost);
		}

		return dbPost;
	}


	@Transactional
	public void unschedule(Integer postId) {
		try {
			scheduler.unscheduleJob(new TriggerKey("trigger-" + postId));
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void schedule(Integer postId, Date scheduledDate) {
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger-" + postId, "schedules").startAt(scheduledDate).build();
		TriggerKey triggerKey = new TriggerKey("trigger-" + postId);

		try {
			if (scheduler.checkExists(triggerKey)) {
				scheduler.rescheduleJob(triggerKey, trigger);
			} else {
				JobDetail job = JobBuilder.newJob(PostScheduleJob.class).withIdentity("schedule-" + postId, "schedules").build();
				job.getJobDataMap().put("postId", String.valueOf(postId)); //must send as string because useProperties is set true

				scheduler.scheduleJob(job, trigger);
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void buildNotification(Post post) {
		Notification notification = new Notification();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.station = post.station;
		notification.post = post;
		notification.message = post.title;
		notification.person = authProvider.getLoggedPerson();

		try {
			if (post.station != null) {
				asyncService.notifyAndroid(TenantContextHolder.getCurrentTenantId(), post.station.id, notification);
				asyncService.notifyApple(TenantContextHolder.getCurrentTenantId(), post.station.id, notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Transactional(noRollbackFor = Exception.class)
	public void countPostRead(Post post, Person person, String sessionId) {
		QPostRead pr = QPostRead.postRead;
		if (person == null || person.username.equals("wordrails")) {
			if(postReadRepository.findAll(pr.sessionid.eq(sessionId).and(pr.post.id.eq(post.id)))
					.iterator().hasNext()) {
				return;
			}
		} else {
			if(postReadRepository.findAll(pr.sessionid.eq("0").and(pr.post.id.eq(post.id)).and(pr.person.id.eq(person.id)))
					.iterator().hasNext()) {
				return;
			}
		}

		PostRead postRead = new PostRead();
		postRead.person = person;
		postRead.post = post;
		postRead.sessionid = "0"; // constraint fails if null
		if (postRead.person != null && postRead.person.username.equals("wordrails")) { // if user wordrails, include session to uniquely identify the user.
			postRead.person = null;
			postRead.sessionid = sessionId;
		}

		try {
			postReadRepository.save(postRead);
			queryPersistence.incrementReadsCount(post.id);
		} catch (ConstraintViolationException | DataIntegrityViolationException e) {
			log.info("user already read this post");
		}
	}
}
