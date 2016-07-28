package co.xarx.trix.config;

import co.xarx.trix.annotation.IntegrationTestBean;
import co.xarx.trix.config.modelmapper.*;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.multitenancy.TenantProvider;
import co.xarx.trix.elasticsearch.mapper.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

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
public class ApplicationTestConfig{

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

	@Bean
	public static PropertySourcesPlaceholderConfigurer getProperties() {
		return new PropertySourcesPlaceholderConfigurer();
	}

}