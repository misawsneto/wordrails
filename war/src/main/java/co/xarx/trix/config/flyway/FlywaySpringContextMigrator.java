package co.xarx.trix.config.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class FlywaySpringContextMigrator {

	@Autowired
	private Flyway flyway;
	@Autowired
	private ApplicationContext applicationContext;

	private static AutowireCapableBeanFactory factory = null;

	@PostConstruct
	public void postConstruct() {
		factory = applicationContext.getAutowireCapableBeanFactory();
	}

	public void migrate() {
		flyway.setLocations("db.migration");
		flyway.migrate();
	}

	public static void autowire(Object object) {
		if (factory == null) {
			throw new IllegalStateException("Spring Autowire Capable Bean Factory has not been configured");
		}
		factory.autowireBean(object);
	}

}