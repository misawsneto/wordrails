package co.xarx.trix.config.spring;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@ComponentScan(basePackages = "co.xarx.trix")
public class ApplicationConfig {

	@Bean
	public ObjectMapper simpleMapper() {
		return new ObjectMapper();
	}
}
