package com.wordrails.jobs;

import com.wordrails.GCMService;
import com.wordrails.business.Notification;
import com.wordrails.business.Post;
import com.wordrails.business.Station;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.StationRepository;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class PostScheduleJob extends QuartzJobBean {

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private GCMService gcmService;
	@Autowired
	private StationRepository stationRepository;


	@Override
	protected void executeInternal(JobExecutionContext context) {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();

		Integer id = Integer.valueOf(dataMap.getString("postId"));

		System.out.println("SCHEDULED POST: " + id);

		Post post = postRepository.findOne(id);
		if (post != null) {
			post.state = Post.STATE_PUBLISHED;
			postRepository.save(post);

			if (post.notify) {
				buildNotification(post);
			}
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