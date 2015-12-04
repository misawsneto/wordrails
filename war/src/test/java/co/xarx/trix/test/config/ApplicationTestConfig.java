package co.xarx.trix.test.config;

import co.xarx.trix.config.spring.PropertyConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@Configuration
@Import({PropertyConfig.class})
@ComponentScan(basePackages = {
		"co.xarx.trix.elasticsearch",
		"co.xarx.trix.domain"
}
)
public class ApplicationTestConfig {

	@Bean
	public ObjectMapper simpleMapper() {
		return new ObjectMapper();
	}
}
