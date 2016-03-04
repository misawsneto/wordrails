package co.xarx.trix.config.flyway;

import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.integrator.spi.Integrator;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.service.spi.SessionFactoryServiceRegistry;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FlywayIntegrator implements Integrator, ApplicationContextAware {

	public final Logger logger = Logger.getLogger(getClass().getSimpleName());

	private static ApplicationContext applicationContext;

	@Override
	public void integrate(final Configuration configuration, final SessionFactoryImplementor sessionFactoryImplementor,
						  final SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {

		Environment env = applicationContext.getEnvironment();
		Boolean migrate = env.getRequiredProperty("trix.flyway.migrate", Boolean.class);
		String baselineVersion = env.getRequiredProperty("trix.flyway.baselineVersion", String.class);
		DataSource dataSource = (DataSource) applicationContext.getBean("dataSource");

		if (migrate) {
			logger.log(Level.INFO, "Starting Flyway Migration");
			Flyway flyway = new Flyway();
			flyway.setDataSource(dataSource);
			flyway.setBaselineVersionAsString(baselineVersion);
			flyway.setOutOfOrder(true);

			//yes, this is a hack. not my fault setPlaceholderReplacement doesnt work
			flyway.setPlaceholderPrefix("NDSIJbhasiBDysBDSAB");
			flyway.setPlaceholderSuffix("HBDUDBYUDVYludinauidaiu");

			flyway.migrate();
			logger.log(Level.INFO, "Finished Flyway Migration");
		} else {
			logger.log(Level.INFO, "Flyway migration is disabled");
		}
	}

	@Override
	public void integrate(final MetadataImplementor metadataImplementor, final SessionFactoryImplementor sessionFactoryImplementor, final SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
		//no-op
	}

	@Override
	public void disintegrate(final SessionFactoryImplementor sessionFactoryImplementor, final SessionFactoryServiceRegistry sessionFactoryServiceRegistry) {
		//no-op
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		FlywayIntegrator.applicationContext = applicationContext;
	}

	public static void autowire(SpringContextMigration migration) {
		applicationContext.getAutowireCapableBeanFactory().autowireBean(migration);
	}
}