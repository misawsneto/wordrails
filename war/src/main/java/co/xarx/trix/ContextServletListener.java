package co.xarx.trix;

import java.util.Random;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class ContextServletListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Random random = new Random();
		int buildNumber = random.nextInt();
		if(buildNumber < 0)
			buildNumber = -buildNumber;
		sce.getServletContext().setAttribute("buildNumber", buildNumber);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}