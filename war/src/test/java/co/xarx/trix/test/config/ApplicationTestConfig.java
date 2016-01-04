package co.xarx.trix.test.config;

import co.xarx.trix.config.spring.PropertyConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PropertyConfig.class})
@ComponentScan(basePackages = {
		"co.xarx.trix.elasticsearch",
		"co.xarx.trix.domain"
}
)
public class ApplicationTestConfig {
}
