package co.xarx.trix.config;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.config.modelmapper.*;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.multitenancy.TenantProvider;
import co.xarx.trix.elasticsearch.mapper.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

import static org.springframework.context.annotation.FilterType.ANNOTATION;

@Configuration
@EnableAsync
@EnableAspectJAutoProxy
@ComponentScan(
		basePackages = {"co.xarx.trix"},
		useDefaultFilters = false,
		includeFilters = {
				@ComponentScan.Filter(type = ANNOTATION, value = {IntegrationTestBean.class})
		})
public class ApplicationTestConfig implements AsyncConfigurer{

	@Bean
	public ObjectMapper simpleMapper() {
		return new ObjectMapper();
	}

	@Bean
	public TenantProvider tenantProvider() {
		return TenantContextHolder::getCurrentTenantId;
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PersonMap());
		modelMapper.addMappings(new PostMap());
		modelMapper.addMappings(new StationMap());
		modelMapper.addMappings(new CategoryDataMap());
		modelMapper.addMappings(new PictureDataMap());
		modelMapper.addMappings(new ImageDataMap());
		modelMapper.addMappings(new PostImageDataMap());
		modelMapper.addMappings(new PersonDataMap());
		modelMapper.addMappings(new PostDataMap());
		modelMapper.addMappings(new PageDataMap());
		modelMapper.addMappings(new VideoDataMap());
		modelMapper.addMappings(new StationViewMap());
		modelMapper.addMappings(new PostViewMap());
		return modelMapper;
	}

	@Override
	public Executor getAsyncExecutor() {
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(5);
		executor.setMaxPoolSize(20);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("Async-");
		executor.initialize();
		return executor;
	}

	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return new AsyncUncaughtExceptionHandler() {
			@Override
			public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
				throwable.printStackTrace();
				TenantContextHolder.setCurrentTenantId(null);
			}
		};
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer getProperties() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}