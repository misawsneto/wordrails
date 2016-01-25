package co.xarx.trix.test.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {
		"co.xarx.trix.elasticsearch",
		"co.xarx.trix.domain"
}
)
public class ApplicationTestConfig {
}
