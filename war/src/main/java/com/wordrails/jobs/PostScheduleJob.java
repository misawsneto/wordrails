package com.wordrails.jobs;

import com.wordrails.domain.Post;
import com.wordrails.domain.PostScheduled;
import com.wordrails.services.PostService;
import com.wordrails.persistence.PostScheduledRepository;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
public class PostScheduleJob extends QuartzJobBean {

	@Autowired
	private PostService postService;
	@Autowired
	private PostScheduledRepository postScheduledRepository;


	@Override
	protected void executeInternal(JobExecutionContext context) {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();

		Integer id = Integer.valueOf(dataMap.getString("postId"));

		System.out.println("SCHEDULED POST: " + id);

		PostScheduled scheduledPost = postScheduledRepository.findOne(id);
		if (scheduledPost != null) {
			if (scheduledPost.notify) {
				postService.buildNotification(scheduledPost);
			}

			scheduledPost.date = new Date();
			postScheduledRepository.save(scheduledPost);
			postService.convertPost(id, Post.STATE_PUBLISHED);
		}
	}


}