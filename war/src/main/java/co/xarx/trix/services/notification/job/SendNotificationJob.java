package co.xarx.trix.services.notification.job;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.services.notification.NotificationService;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Service;


@Service
public class SendNotificationJob extends QuartzJobBean implements InterruptableJob, Job {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private Scheduler scheduler;
	@Autowired
	private NotificationService notificationService;

	private JobKey jobKey;


	@Override
	protected void executeInternal(JobExecutionContext context) {
		this.jobKey = context.getJobDetail().getKey();
		JobDataMap dataMap = context.getJobDetail().getJobDataMap();
		String tenantId = dataMap.getString("tenantId");
		TenantContextHolder.setCurrentTenantId(tenantId);

		String title = dataMap.getString("title");
		String message = dataMap.getString("message");
		Integer id = Integer.valueOf(dataMap.getString("postId"));
		log.info("SENDING NOTIFICATION FOR POST: " + id);

		notificationService.createPostNotification(title, message, id);
	}


	@Override
	public void interrupt() throws UnableToInterruptJobException {
		scheduler.interrupt(jobKey);
	}
}