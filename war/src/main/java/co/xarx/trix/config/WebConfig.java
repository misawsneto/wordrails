package co.xarx.trix.config;

import co.xarx.trix.ResteasyApplication;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.jboss.resteasy.plugins.spring.SpringBeanProcessor;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.springmvc.ResteasyHandlerAdapter;
import org.jboss.resteasy.springmvc.ResteasyHandlerMapping;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.ServletContextInitializer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletException;
import java.util.Arrays;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	@Bean
	public ServletContextInitializer initializer() {
		return new ServletContextInitializer() {
			@Override
			public void onStartup(ServletContext servletContext) throws ServletException {
//				servletContext.setInitParameter("resteasy.scan", "true");
				servletContext.setInitParameter("resteasy.servlet.mapping.prefix", "/api");
			}
		};
	}

	@ConditionalOnMissingBean(ResteasyDeployment.class)
	@ConfigurationProperties(prefix = "resteasy.deployment")
	@Bean(initMethod = "start", destroyMethod = "stop")
	public ResteasyDeployment resteasyDeployment(final SpringBeanProcessor springBeanProcessor) {
		ResteasyDeployment resteasyDeployment = new ResteasyDeployment() {
			public void start() {
				super.start();
				if (springBeanProcessor.getRegistry() == null) {
					springBeanProcessor.setRegistry(this.getRegistry());
				}
			}
		};
		resteasyDeployment.setProviderFactory(springBeanProcessor.getProviderFactory());
		return resteasyDeployment;
	}

	@ConditionalOnMissingBean(SpringBeanProcessor.class)
	@Bean
	public SpringBeanProcessor springBeanProcessor() {
		SpringBeanProcessor springBeanProcessor = new SpringBeanProcessor();
		springBeanProcessor.setProviderFactory(new ResteasyProviderFactory());
		return springBeanProcessor;
	}

	@ConditionalOnMissingBean(ResteasyHandlerMapping.class)
	@Bean
	public ResteasyHandlerMapping resteasyHandlerMapper(ResteasyDeployment deployment) {
		ResteasyHandlerMapping handlerMapping = new ResteasyHandlerMapping(deployment);
		handlerMapping.setOrder(0);
		return handlerMapping;
	}

	@ConditionalOnMissingBean(ResteasyHandlerAdapter.class)
	@Bean
	public ResteasyHandlerAdapter resteasyHandlerAdapter(ResteasyDeployment deployment) {
		return new ResteasyHandlerAdapter(deployment);
	}

	@Bean
	public ServletContextListener restEasyBootstrap() {
		return new ResteasyBootstrap();
	}

	@Bean
	public FilterRegistrationBean restEasyServlet() {
		final FilterRegistrationBean filterReg = new FilterRegistrationBean();
		filterReg.setFilter(new FilterDispatcher());
		filterReg.setName("resteasy-servlet");
		filterReg.setOrder(Integer.MIN_VALUE + 1);
		filterReg.setUrlPatterns(Arrays.asList("/api/*"));
		filterReg.addInitParameter("javax.ws.rs.Application", ResteasyApplication.class.getName());
		return filterReg;
	}
}