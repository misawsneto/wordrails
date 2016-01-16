package co.xarx.trix.services;

import co.xarx.trix.scheduler.jobs.PostScheduleJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class SchedulerService {

	@Autowired
	private Scheduler scheduler;


	@Transactional
	public void unschedule(Integer postId) {
		TriggerKey triggerKey = new TriggerKey("trigger-" + postId);
		try {
			if (scheduler.checkExists(triggerKey)) {
				scheduler.unscheduleJob(new TriggerKey("trigger-" + postId));
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
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
}
