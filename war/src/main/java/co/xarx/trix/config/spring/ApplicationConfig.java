package co.xarx.trix.config.spring;

import co.xarx.trix.elasticsearch.mapper.PersonMap;
import co.xarx.trix.elasticsearch.mapper.PostMap;
import co.xarx.trix.elasticsearch.mapper.PostViewMap;
import co.xarx.trix.elasticsearch.mapper.StationMap;
import co.xarx.trix.services.AmazonCloudService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@PropertySource(name = "props",
		value = {"classpath:application.properties",
				"classpath:application_${spring.profiles.active:dev}.properties"
		}
)
@ComponentScan(basePackages = "co.xarx.trix")
public class ApplicationConfig {

	@Value("${amazon.accessKey}")
	private String accessKey;
	@Value("${amazon.accessSecretKey}")
	private String accessSecretKey;
	@Value("${amazon.cloudfrontUrl}")
	private String cloudfrontUrl;
	@Value("${amazon.bucketName}")
	private String bucketName;

	@Bean
	public ObjectMapper simpleMapper() {
		return new ObjectMapper();
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.addMappings(new PersonMap());
		modelMapper.addMappings(new PostMap());
		modelMapper.addMappings(new StationMap());
		modelMapper.addMappings(new PostViewMap());
		return modelMapper;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer getProperties() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	@Bean
	public AmazonCloudService amazonCloudService() {
		return new AmazonCloudService(accessKey, accessSecretKey, cloudfrontUrl, bucketName);
	}
}