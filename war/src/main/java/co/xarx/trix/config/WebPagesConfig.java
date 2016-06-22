//package co.xarx.trix.config;
//
//import org.springframework.boot.context.embedded.ServletRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.DispatcherServlet;
//import org.springframework.web.servlet.config.annotation.EnableWebMvc;
//import org.springframework.web.servlet.view.InternalResourceViewResolver;
//
//@Configuration
//@EnableWebMvc
//public class WebPagesConfig {
//
//	@Bean
//	public DispatcherServlet dispatcherServlet() {
//		return new DispatcherServlet();
//	}
//
//	@Bean
//	public ServletRegistrationBean restEasyServlet() {
//		final ServletRegistrationBean registrationBean = new ServletRegistrationBean(dispatcherServlet(), "/settings/*");
//		registrationBean.setLoadOnStartup(1);
//		return registrationBean;
//	}
//
//	@Bean
//	public InternalResourceViewResolver viewResolver() {
//		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
//		resolver.setPrefix("/webapp/");
//		resolver.setSuffix(".jsp");
//		return resolver;
//	}
//}