package com.wordrails.business;

import com.wordrails.GCMService;
import com.wordrails.jobs.PostScheduleJob;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.QueryPersistence;
import com.wordrails.persistence.StationRepository;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
	private GCMService gcmService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private StationRepository stationRepository;

	public void convertPost(int postId, String state) {
		Post dbPost = postRepository.findOne(postId);

		if (dbPost != null) {
			log.debug("Before convert: " + dbPost.getClass().getSimpleName());
			if (state.equals(dbPost.state)) {
				return; //they are the same type. no need for convertion
			}

			if (dbPost.state.equals(Post.STATE_SCHEDULED)) { //if converting FROM scheduled, unschedule
				unschedule(dbPost.id);
			} else if (state.equals(Post.STATE_SCHEDULED)) { //if converting TO scheduled, schedule
				schedule(dbPost.id, dbPost.scheduledDate);
			}

			//queryPersistence.changePostState(postId, state);

			dbPost.state = state;
			postRepository.save(dbPost);
			log.debug("After convert: " + dbPost.getClass().getSimpleName());
		}
	}


	@Transactional
	public void unschedule(Integer postId) {
		try {
			scheduler.unscheduleJob(new TriggerKey("trigger-" + postId));
		} catch (SchedulerException e) {
		}
	}

	@Transactional
	public void schedule(Integer postId, Date scheduledDate) {
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger-" + postId, "schedules").startAt(scheduledDate).build();
		TriggerKey triggerKey = new TriggerKey("trigger-" + postId);

		try {
			if (scheduler.checkExists(triggerKey)) {
				scheduler.rescheduleJob(triggerKey, trigger);
			}

			JobDetail job = JobBuilder.newJob(PostScheduleJob.class).withIdentity("schedule-" + postId, "schedules").build();
			job.getJobDataMap().put("postId", String.valueOf(postId)); //must send as string because useProperties is set true

			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void buildNotification(Post post) {
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
