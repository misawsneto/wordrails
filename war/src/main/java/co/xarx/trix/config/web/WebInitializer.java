package co.xarx.trix.config.web;

import org.springframework.web.WebApplicationInitializer;

import javax.servlet.ServletContext;

public class WebInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) {
		container.addListener(co.xarx.trix.ContextServletListener.class);
		container.addListener(co.xarx.trix.config.web.SpringResteasyContextLoaderListener.class);
	}
}