package co.xarx.trix;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Date;
import java.util.Random;

public class ContextServletListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		Random random = new Random();
		int buildNumber = random.nextInt();
		if(buildNumber < 0)
			buildNumber = -buildNumber;
		sce.getServletContext().setAttribute("buildNumber", buildNumber);
		sce.getServletContext().setAttribute("lastModified", new Date().toGMTString());
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {

	}
}