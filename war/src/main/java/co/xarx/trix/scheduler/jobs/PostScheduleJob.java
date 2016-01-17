package co.xarx.trix.scheduler.jobs;

import co.xarx.trix.services.PostService;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class PostScheduleJob extends QuartzJobBean {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private PostService postService;


	@Override
	protected void executeInternal(JobExecutionContext context) {
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		Integer id = Integer.valueOf(dataMap.getString("postId"));
		log.info("SCHEDULED POST: " + id);

		postService.publishScheduledPost(id);
	}


}