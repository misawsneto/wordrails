package com.wordrails.business;

import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.notification.APNService;
import com.wordrails.notification.GCMService;
import com.wordrails.jobs.PostScheduleJob;
import com.wordrails.persistence.PostDraftRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.PostScheduledRepository;
import com.wordrails.persistence.PostTrashRepository;
import com.wordrails.persistence.QueryPersistence;
import com.wordrails.persistence.StationRepository;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
public class PostService {

	private static final Logger log = LoggerFactory.getLogger(PostService.class);

	@Autowired private QueryPersistence queryPersistence;
	@Autowired private Scheduler scheduler;
	@Autowired private GCMService gcmService;
	@Autowired private APNService apnService;
	@Autowired private PostRepository postRepository;
	@Autowired private StationRepository stationRepository;
	
	@Autowired private PostDraftRepository postDraftRepository;
	
	@Autowired private PostScheduledRepository postScheduledRepository;
	
	@Autowired private PostTrashRepository postTrashRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;

	@PersistenceContext
	private EntityManager manager;

	public void removePostIndex(Post post){
		FullTextEntityManager ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
		ftem.purge(Post.class, post.id);
	}
	
	public void updatePostIndex (Post post){
		FullTextEntityManager ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
		ftem.index(post);
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
			removePostIndex(dbPost);
//			updatePostIndex(dbPost);
		}

		return dbPost;
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
			} else {
				JobDetail job = JobBuilder.newJob(PostScheduleJob.class).withIdentity("schedule-" + postId, "schedules").build();
				job.getJobDataMap().put("postId", String.valueOf(postId)); //must send as string because useProperties is set true

				scheduler.scheduleJob(job, trigger);
			}
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
		notification.person = authProvider.getLoggedPerson();
		try {
			if (post.station != null && post.station.networks != null) {
				Station station = stationRepository.findOne(post.station.id);
				gcmService.sendToStation(station.id, notification);

				Network network = authProvider.getNetwork();
				apnService.sendToStation(network, station.id, notification);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}