package co.xarx.trix.services;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

@Service
public class SchedulerService {

	@Autowired
	private Scheduler scheduler;


	@Transactional
	public void unschedule(Integer key) throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey("trigger-" + key);
		if (scheduler.checkExists(triggerKey)) {
			scheduler.unscheduleJob(new TriggerKey("trigger-" + key));
		}
	}

	@Transactional
	public <T extends Job> void schedule(String key, Class<T> jobClass, Date scheduledDate, Map<String, String>
			properties)
			throws
			SchedulerException {
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity("trigger-" + key, "schedules").startAt(scheduledDate).build();
		TriggerKey triggerKey = new TriggerKey("trigger-" + key);

		if (scheduler.checkExists(triggerKey)) {
			scheduler.rescheduleJob(triggerKey, trigger);
		} else {
			JobDetail job = JobBuilder.newJob(jobClass).withIdentity("schedule-" + key, "schedules").build();
			for (String s : properties.keySet()) {
				job.getJobDataMap().put(s, properties.get(s));
			}

			scheduler.scheduleJob(job, trigger);
		}
	}
}