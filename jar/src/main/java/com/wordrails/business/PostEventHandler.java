package com.wordrails.business;

import com.wordrails.GCMService;
import com.wordrails.jobs.PostScheduleJob;
import com.wordrails.persistence.*;
import com.wordrails.security.PostAndCommentSecurityChecker;
import com.wordrails.util.WordrailsUtil;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RepositoryEventHandler(Post.class)
@Component
public class PostEventHandler {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostReadRepository postReadRepository;
	@Autowired
	private CellRepository cellRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private PromotionRepository promotionRepository;
	@Autowired
	private PostAndCommentSecurityChecker postAndCommentSecurityChecker;
	@Autowired
	private GCMService gcmService;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private BookmarkRepository bookmarkRepository;
	@Autowired
	private RecommendRepository recommendRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private Scheduler scheduler;

	@HandleBeforeCreate
	public void handleBeforeCreate(Post post) throws UnauthorizedException, NotImplementedException {

		if (postAndCommentSecurityChecker.canWrite(post)) {
			Date now = new Date();
			if (post.date == null) {
				post.date = now;
			}

			if (post.slug == null || post.slug.isEmpty()) {
				String originalSlug = WordrailsUtil.toSlug(post.title);
				post.originalSlug = originalSlug;
				try {
					post.slug = originalSlug;
					postRepository.save(post);
				} catch (org.springframework.dao.DataIntegrityViolationException ex) {
					String hash = WordrailsUtil.generateRandomString(5, "Aa#u");
					post.slug = originalSlug + "-" + hash;
				}
			} else {
				post.originalSlug = post.slug;
			}

		} else {
			throw new UnauthorizedException();
		}
	}

	@HandleAfterCreate
	@Transactional
	public void handleAfterCreate(Post post) {
		if(post.originalPostId == null || post.originalPostId <= 0) {
			post.originalPostId = post.id;
		}

		if (post.notify && post.state.equals(Post.STATE_PUBLISHED)) {
			buildNotification(post);
		}
	}

	@HandleAfterSave
	@Transactional
	public void handleAfterSave(Post post) {
		if (post.state.equals(Post.STATE_SCHEDULED)) {
			schedule(post.id, post.scheduledDate);
		}
	}

	@Transactional
	private void schedule(Integer postId, Date scheduledDate) {
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger-" + postId, "schedules").startAt(scheduledDate).build();
		TriggerKey triggerKey = new TriggerKey("trigger-" + postId);

		try {
			if(scheduler.checkExists(triggerKey)) {
				scheduler.rescheduleJob(triggerKey, trigger);
			}

			JobDetail job = JobBuilder.newJob(PostScheduleJob.class).withIdentity("schedule-" + postId, "schedules").build();
			job.getJobDataMap().put("postId", String.valueOf(postId)); //must send as string because useProperties is set true

			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@HandleBeforeDelete
	@Transactional
	public void handleBeforeDelete(Post post) throws UnauthorizedException {
		if (postAndCommentSecurityChecker.canRemove(post)) {

			List<Image> images = imageRepository.findByPost(post);
			if (images != null && images.size() > 0) {
				postRepository.updateFeaturedImagesToNull(images);
			}
			imageRepository.delete(images);
			cellRepository.delete(cellRepository.findByPost(post));
			commentRepository.delete(post.comments);
			promotionRepository.delete(post.promotions);
			postReadRepository.deleteByPost(post);
			notificationRepository.deleteByPost(post);
			bookmarkRepository.deleteByPost(post);
			recommendRepository.deleteByPost(post);
		} else {
			throw new UnauthorizedException();
		}
	}

	private void buildNotification(Post post) {
		Notification notification = new Notification();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.station = post.station;
		notification.post = post;
		notification.message = post.title;
		try {
			if (post.station != null && post.station.networks != null) {
				Station station = stationRepository.findOne(post.station.id);
				gcmService.sendToStation(station.id, notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}