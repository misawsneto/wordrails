package co.xarx.trix.scheduler.jobs;

import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.PostScheduled;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.PostService;
import co.xarx.trix.persistence.PostScheduledRepository;
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
	private MobileService mobileService;
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
				mobileService.buildNotification(scheduledPost);
			}

			scheduledPost.date = new Date();
			postScheduledRepository.save(scheduledPost);
			postService.convertPost(id, Post.STATE_PUBLISHED);
		}
	}


}