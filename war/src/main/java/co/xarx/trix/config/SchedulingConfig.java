package co.xarx.trix.config;

import co.xarx.trix.scheduler.AutowiringSpringBeanJobFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@Configuration
@EnableScheduling
public class SchedulingConfig {

	@Bean
	public SchedulerFactoryBean quartzScheduler(DataSource dataSource,
												PlatformTransactionManager transactionManager,
												ApplicationContext applicationContext) {
		SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
		schedulerFactoryBean.setQuartzProperties(quartzProperties());
		schedulerFactoryBean.setDataSource(dataSource);
		schedulerFactoryBean.setAutoStartup(true);
		schedulerFactoryBean.setTransactionManager(transactionManager);
		AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
		jobFactory.setApplicationContext(applicationContext);
		schedulerFactoryBean.setJobFactory(jobFactory);

		return schedulerFactoryBean;
	}

	@Bean
	public Properties quartzProperties() {
		PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
		propertiesFactoryBean.setLocation(new ClassPathResource("quartz.properties"));
		Properties properties;

		try {
			propertiesFactoryBean.afterPropertiesSet();
			properties = propertiesFactoryBean.getObject();
		} catch (IOException e) {
			throw new RuntimeException("Unable to load quartz.properties", e);
		}

		return properties;
	}
}
