//package co.xarx.trix.config;
//
//import co.xarx.trix.config.multitenancy.TenantInstanceInterceptor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
//import org.springframework.web.servlet.handler.SimpleUrlHandlerMapping;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@Configuration
//public class WebConfig extends WebMvcConfigurerAdapter {
//
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(getTenantInstanceInterceptor());
//		super.addInterceptors(registry);
//	}
//
//	@Bean
//	public SimpleUrlHandlerMapping simpleUrlHandlerMapping() {
//		SimpleUrlHandlerMapping simpleUrlHandlerMapping = new SimpleUrlHandlerMapping();
//		Map map  = new HashMap<>();
//		map.put("/**", getTenantInstanceInterceptor());
//		simpleUrlHandlerMapping.setUrlMap(map);
//		return simpleUrlHandlerMapping;
//	}
//}