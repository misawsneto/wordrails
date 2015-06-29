package com.wordrails.jobs;

import com.wordrails.GCMService;
import com.wordrails.business.Notification;
import com.wordrails.business.Post;
import com.wordrails.business.PostScheduled;
import com.wordrails.business.Station;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.PostScheduledRepository;
import com.wordrails.persistence.StationRepository;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class PostScheduleJob extends QuartzJobBean {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostScheduledRepository postScheduledRepository;
	@Autowired
	private GCMService gcmService;
	@Autowired
	private StationRepository stationRepository;


	@Override
	protected void executeInternal(JobExecutionContext context) {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();

		Integer id = Integer.valueOf(dataMap.getString("postId"));

		System.out.println("SCHEDULED POST: " + id);

		PostScheduled scheduledPost = postScheduledRepository.findOne(id);
		if (scheduledPost != null) {
			if (scheduledPost.notify) {
				buildNotification(scheduledPost);
			}

			Post post = new Post();
			post.convertSubtype(scheduledPost);

			postScheduledRepository.delete(scheduledPost);
			postRepository.save(post);
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