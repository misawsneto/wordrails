package co.xarx.trix.scheduler.jobs;

import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.services.post.PostService;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;


@Service
public class PostScheduleJob extends QuartzJobBean implements InterruptableJob {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private Scheduler scheduler;
	@Autowired
	private PostService postService;

	private JobKey jobKey;


	@Override
	protected void executeInternal(JobExecutionContext context) {
		this.jobKey = context.getJobDetail().getKey();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Integer id = Integer.valueOf(dataMap.getString("postId"));
		log.info("SCHEDULED POST: " + id);

		try {
			postService.publishScheduledPost(id, true);
		} catch (NotificationException e) {
			postService.publishScheduledPost(id, false);
		}
	}


	@Override
	public void interrupt() throws UnableToInterruptJobException {
		scheduler.interrupt(jobKey);
	}
}