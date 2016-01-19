package co.xarx.trix.test.config;

import co.xarx.trix.aspect.MultitenantRepositoryAspect;
import co.xarx.trix.aspect.ProfilerAspect;
import co.xarx.trix.config.spring.PropertyConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAspectJAutoProxy
@Import({PropertyConfig.class})
//@ComponentScan(
//		basePackages = {"co.xarx.trix.aspect"},
//		excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "co.xarx.trix.config.spring.*")
//)
public class ApplicationTestConfig {

	@Bean
	public ProfilerAspect profilerAspect() {
		return new ProfilerAspect();
	}

	@Bean
	public MultitenantRepositoryAspect multitenantRepositoryAspect() {
		return new MultitenantRepositoryAspect();
	}

	static {
		System.setProperty("indexES", "false");
		System.setProperty("dbName", "trix_dev");
	}
}
