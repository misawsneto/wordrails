package com.wordrails.business;

import com.wordrails.GCMService;
import com.wordrails.jobs.PostScheduleJob;
import com.wordrails.persistence.*;
import com.wordrails.security.PostAndCommentSecurityChecker;
import com.wordrails.util.WordrailsUtil;
import org.joda.time.DateTime;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@RepositoryEventHandler(Post.class)
@Component
public class PostEventHandler {

	private @Autowired
	PostRepository postRepository;
	@Autowired
	private WordpressService wordpressService;
	private @Autowired
	PostReadRepository postReadRepository;
	private @Autowired
	CellRepository cellRepository;
	private @Autowired
	CommentRepository commentRepository;
	private @Autowired
	ImageRepository imageRepository;
	private @Autowired
	PromotionRepository promotionRepository;
	private @Autowired
	PostAndCommentSecurityChecker postAndCommentSecurityChecker;
	private @Autowired GCMService gcmService;

	private @Autowired StationRepository stationRepository;

	private @Autowired FavoriteRepository favoriteRepository;
	private @Autowired BookmarkRepository bookmarkRepository;
	private @Autowired RecommendRepository recommendRepository;
	private @Autowired NotificationRepository notificationRepository;

	@Autowired
	private Scheduler sched;

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
				} catch (DataIntegrityViolationException ex) {
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
		if (post.state.equals(Post.STATE_SCHEDULED)) {
			JobDetail job = newJob(PostScheduleJob.class)
					.withIdentity("schedule-" + post.id, "schedules")
					.build();

			//must send as string because useProperties is set true
			job.getJobDataMap().put("postId", String.valueOf(post.id));

			Trigger trigger = newTrigger()
					.withIdentity("trigger-" + post.id, "schedules")
					.startAt(post.scheduledDate)
					.build();

			try {
				sched.scheduleJob(job, trigger);
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
		} else if (post.notify && post.state.equals(Post.STATE_PUBLISHED))
			buildNotification(post);
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
			favoriteRepository.deleteByPost(post);
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
		try{
			if(post.station != null && post.station.networks != null){
				Station station = stationRepository.findOne(post.station.id);
				gcmService.sendToStation(station.id, notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}