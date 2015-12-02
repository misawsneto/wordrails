package co.xarx.trix.config;

import org.jboss.resteasy.plugins.spring.SpringContextLoaderSupport;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

public class TrixContextLoaderListener extends ContextLoaderListener {

	public TrixContextLoaderListener(WebApplicationContext context) {
		super(context);
	}

	@Override
	protected void customizeContext(ServletContext servletContext, ConfigurableWebApplicationContext configurableWebApplicationContext) {
		super.customizeContext(servletContext, configurableWebApplicationContext);
		
		SpringContextLoaderSupport springContextLoaderSupport = new SpringContextLoaderSupport();
		springContextLoaderSupport.customizeContext(servletContext, configurableWebApplicationContext);
	}
}