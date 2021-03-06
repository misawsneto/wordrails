package co.xarx.trix.config.web;

import org.jboss.resteasy.plugins.spring.SpringContextLoaderSupport;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;

public class SpringResteasyContextLoaderListener extends ContextLoaderListener {

	@Override
	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext configurableWebApplicationContext) {
		super.customizeContext(servletContext, configurableWebApplicationContext);
		
		SpringContextLoaderSupport springContextLoaderSupport = new SpringContextLoaderSupport();
		springContextLoaderSupport.customizeContext(servletContext, configurableWebApplicationContext);
	}
}