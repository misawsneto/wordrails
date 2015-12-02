package co.xarx.trix.config.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@PropertySource(value = {
		"classpath:application.properties",
		"classpath:application-${spring.profiles.active:default}.properties"
})
public class PropertyConfig {

	@Bean
	public PropertySourcesPlaceholderConfigurer getProperties() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}