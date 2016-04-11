package co.xarx.trix.scheduler;

import org.springframework.scheduling.quartz.CronTriggerFactoryBean;

public class PersistableCronTriggerFactoryBean extends CronTriggerFactoryBean {

	@Override
	public void afterPropertiesSet() {
		try {
			super.afterPropertiesSet();
		} catch (Exception e) {
			e.printStackTrace();
		}

		//Remove the JobDetail element
		getJobDataMap().remove("jobDetail");
	}
}