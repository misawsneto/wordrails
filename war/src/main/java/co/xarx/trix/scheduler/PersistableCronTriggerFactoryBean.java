package co.xarx.trix.scheduler;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailAwareTrigger;

public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean {

	@Override
	public void afterPropertiesSet() {
		try {
			super.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Remove the JobDetail element
		getJobDataMap().remove(JobDetailAwareTrigger.JOB_DETAIL_KEY);
	}
}