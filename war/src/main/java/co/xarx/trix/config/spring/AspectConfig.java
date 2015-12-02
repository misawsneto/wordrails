package co.xarx.trix.config.spring;

import co.xarx.trix.aspect.MultitenantRepositoryAspect;
import co.xarx.trix.aspect.ProfilerAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class AspectConfig {

	@Bean
	public ProfilerAspect profilerAspect() {
		return new ProfilerAspect();
	}

	@Bean
	public MultitenantRepositoryAspect multitenantRepositoryAspect() {
		return new MultitenantRepositoryAspect();
	}
}
