package co.xarx.trix;

import co.xarx.trix.config.TrixContextLoaderListener;
import co.xarx.trix.config.spring.DatabaseConfig;
import co.xarx.trix.config.spring.SecurityConfig;
import co.xarx.trix.web.filter.CORSCustomFilter;
import co.xarx.trix.web.filter.NetworkDomainFilter;
import org.jboss.resteasy.plugins.server.servlet.FilterDispatcher;
import org.jboss.resteasy.plugins.server.servlet.ResteasyBootstrap;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.tuckey.web.filters.urlrewrite.UrlRewriteFilter;

import javax.servlet.*;
import java.util.EnumSet;

public class WebAppInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext context = getAnnotationContext();
		context.register(NetworkDomainFilter.class, WordrailsService.class, SecurityConfig.class, DatabaseConfig.class);

		servletContext.setInitParameter("spring.profiles.active", "dev");
		servletContext.setInitParameter("resteasy.servlet.mapping.prefix", "/api");

		addListeners(servletContext, context);
		addFilters(servletContext);


		addServlets(servletContext, context);
	}

	private void addServlets(ServletContext servletContext, WebApplicationContext context) {
		ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", new DispatcherServlet(context));
		dispatcher.setLoadOnStartup(1);
//		HashMap<String, String> initParameters = new HashMap<>();
//		initParameters.put("contextConfigLocation", "");
//		dispatcher.setInitParameters(initParameters);
		dispatcher.addMapping("/api/*");

//		ServletRegistration.Dynamic index = servletContext.addServlet("myindex", new JspServlet(context));
//		index.addMapping("/*");
//		index.addMapping("/index.jsp");
//		index.addMapping("/search/*");
//		index.addMapping("/post/*");
//		index.addMapping("/bookmarks/*");
//		index.addMapping("/notifications/*");
//		index.addMapping("/settings/*");
//		index.addMapping("/publications/*");
//		index.addMapping("/mystats/*");
//		index.addMapping("/access/createnetwork");
	}

	private void addListeners(ServletContext servletContext, WebApplicationContext context) {
		servletContext.addListener(new ContextServletListener());
		servletContext.addListener(new ResteasyBootstrap());
		servletContext.addListener(new TrixContextLoaderListener(context));
		servletContext.addListener(new RequestContextListener());
		servletContext.addListener(new HttpSessionEventPublisher());
	}

	private void addFilters(ServletContext servletContext) {
		EnumSet<DispatcherType> reqAndForw = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD);

		FilterRegistration.Dynamic repositoryFilter = servletContext.addFilter("springSessionRepositoryFilter", DelegatingFilterProxy.class);
		repositoryFilter.addMappingForUrlPatterns(null, false, "/*");

		FilterRegistration.Dynamic corsCustomFilter = servletContext.addFilter("corsCustomFilter", CORSCustomFilter.class);
		corsCustomFilter.setInitParameter("targetBeanName", "corsCustomFilter");
		corsCustomFilter.addMappingForUrlPatterns(null, false, "/*");

		FilterRegistration.Dynamic springSecurityFilterChain = servletContext.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
		springSecurityFilterChain.addMappingForUrlPatterns(null, false, "/*");

		FilterRegistration.Dynamic pathEntityFilter = servletContext.addFilter("pathEntityFilter", DelegatingFilterProxy.class);
		pathEntityFilter.setInitParameter("targetBeanName", "pathEntityFilter");
		pathEntityFilter.addMappingForUrlPatterns(reqAndForw, false, "/*");

		FilterRegistration.Dynamic urlRewriteFilter = servletContext.addFilter("UrlRewriteFilter", UrlRewriteFilter.class);
		urlRewriteFilter.setInitParameter("logLevel", "slf4j");
		urlRewriteFilter.addMappingForUrlPatterns(reqAndForw, false, "/*");

		FilterRegistration.Dynamic networkDomainFilter = servletContext.addFilter("networkDomainFilter", DelegatingFilterProxy.class);
		networkDomainFilter.setInitParameter("targetBeanName", "networkDomainFilter");
		networkDomainFilter.addMappingForUrlPatterns(reqAndForw, false, "/*");

//		FilterRegistration.Dynamic pathEntityFilter2 = servletContext.addFilter("pathEntityFilter", DelegatingFilterProxy.class);
//		pathEntityFilter2.setInitParameter("targetBeanName", "pathEntityFilter");
//		pathEntityFilter2.addMappingForUrlPatterns(reqAndForw, false, "/*");

		FilterRegistration.Dynamic personDataFilter = servletContext.addFilter("personDataFilter", DelegatingFilterProxy.class);
		personDataFilter.setInitParameter("targetBeanName", "personDataFilter");
		personDataFilter.addMappingForUrlPatterns(reqAndForw, false, "/index.jsp");
		personDataFilter.addMappingForUrlPatterns(reqAndForw, false, "/search/*");
		personDataFilter.addMappingForUrlPatterns(reqAndForw, false, "/post/*");
		personDataFilter.addMappingForUrlPatterns(reqAndForw, false, "/bookmarks/*");
		personDataFilter.addMappingForUrlPatterns(reqAndForw, false, "/notifications/*");
		personDataFilter.addMappingForUrlPatterns(reqAndForw, false, "/settings/*");
		personDataFilter.addMappingForUrlPatterns(reqAndForw, false, "/publications/*");
		personDataFilter.addMappingForUrlPatterns(reqAndForw, false, "/user/*");
		personDataFilter.addMappingForUrlPatterns(reqAndForw, false, "/read/*");
		personDataFilter.addMappingForUrlPatterns(reqAndForw, false, "/mystats/*");

		FilterRegistration.Dynamic inViewFilter = servletContext.addFilter("OpenEntityManagerInViewFilter", OpenEntityManagerInViewFilter.class);
		inViewFilter.setInitParameter("entityManagerFactoryBeanName", "entityManagerFactory");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/api/*");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/index.jsp");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/search/*");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/post/*");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/bookmarks/*");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/notifications/*");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/settings/*");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/publications/*");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/user/*");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/read/*");
		inViewFilter.addMappingForUrlPatterns(reqAndForw, false, "/mystats/*");

		FilterRegistration.Dynamic filterDispatcher = servletContext.addFilter("FilterDispatcher", FilterDispatcher.class);
		filterDispatcher.addMappingForUrlPatterns(null, false, "/api/*");

		FilterRegistration.Dynamic postFilter = servletContext.addFilter("postFilter", DelegatingFilterProxy.class);
		postFilter.setInitParameter("targetBeanName", "postFilter");
		postFilter.addMappingForUrlPatterns(reqAndForw, false, "/api/posts");
		postFilter.addMappingForUrlPatterns(reqAndForw, false, "/api/posts/*");

		FilterRegistration.Dynamic cacheFilter = servletContext.addFilter("cacheFilter", DelegatingFilterProxy.class);
		cacheFilter.setInitParameter("targetBeanName", "cacheFilter");
		cacheFilter.addMappingForUrlPatterns(null, false, "/api/*");
	}

	private XmlWebApplicationContext getXmlContext() {
		XmlWebApplicationContext context = new XmlWebApplicationContext();
		context.setConfigLocation("classpath:applicationContext.xml");
		context.setConfigLocation("classpath:applicationContext-quartz.xml");
		context.setConfigLocation("classpath:applicationContext-integration.xml");
		context.setConfigLocation("classpath:applicationContext-security.xml");
		return context;
	}

	private AnnotationConfigWebApplicationContext getAnnotationContext() {
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation("co.xarx.trix.config.spring");
		return context;
	}
}

