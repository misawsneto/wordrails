package co.xarx.trix.config.flyway;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class FlywayServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		WebApplicationContext applicationContext = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

		FlywaySpringContextMigrator migrator = applicationContext.getBean(FlywaySpringContextMigrator.class);
		migrator.migrate();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}
}