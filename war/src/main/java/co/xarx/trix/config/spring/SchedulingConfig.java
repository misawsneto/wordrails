package co.xarx.trix.config.spring;

import co.xarx.trix.scheduler.AutowiringSpringBeanJobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

@Configuration
@EnableScheduling
public class SchedulingConfig {

	@Autowired
	DataSourceConfig dataSourceConfig;
	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public SchedulerFactoryBean quartzScheduler() {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setConfigLocation(applicationContext.getResource("classpath:quartz.properties"));
		schedulerFactoryBean.setDataSource(dataSourceConfig.dataSource());
		schedulerFactoryBean.setAutoStartup(true);
		schedulerFactoryBean.setApplicationContextSchedulerContextKey("applicationContext");
		schedulerFactoryBean.setJobFactory(new AutowiringSpringBeanJobFactory());

		return schedulerFactoryBean;
	}
}
