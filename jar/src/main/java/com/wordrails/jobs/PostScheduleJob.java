package com.wordrails.jobs;

import com.wordrails.business.Post;
import com.wordrails.business.PostScheduled;
import com.wordrails.business.PostService;
import com.wordrails.persistence.PostScheduledRepository;
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

			postService.convertPost(id, Post.STATE_PUBLISHED);
		}
	}


}