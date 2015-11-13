package co.xarx.trix.config;

import javax.servlet.ServletContext;

import org.jboss.resteasy.plugins.spring.SpringContextLoaderSupport;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

public class WordRailsContextLoaderListener extends ContextLoaderListener {
	@Override
	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext configurableWebApplicationContext) {		
		super.customizeContext(servletContext, configurableWebApplicationContext);
		
		SpringContextLoaderSupport springContextLoaderSupport = new SpringContextLoaderSupport();
		springContextLoaderSupport.customizeContext(servletContext, configurableWebApplicationContext);
	}
}