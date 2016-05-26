package co.xarx.trix.config;

import co.xarx.trix.config.modelmapper.*;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.multitenancy.TenantProvider;
import co.xarx.trix.elasticsearch.mapper.*;
import co.xarx.trix.services.AmazonCloudService;
import co.xarx.trix.services.notification.APNSClient;
import co.xarx.trix.services.notification.GCMClient;
import co.xarx.trix.services.notification.MobileNotificationSender;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gcm.server.Sender;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
@PropertySource(name = "props",
		value = {
				"classpath:application-${spring.profiles.active:dev}.properties"
		}
)
@ComponentScan(basePackages = {"co.xarx.trix", "org.javers.spring.jpa"})
@EnableHypermediaSupport(type = {EnableHypermediaSupport.HypermediaType.HAL})
public class ApplicationConfig {

	@Value("${trix.amazon.key}")
	private String accessKey;
	@Value("${trix.amazon.secret}")
	private String accessSecretKey;
	@Value("${trix.amazon.cloudfront}")
	private String cloudfrontUrl;
	@Value("${trix.amazon.bucket}")
	private String bucketName;
	@Value("${trix.gcm.key}")
	private String gcmKey;

	@Bean
	public ObjectMapper simpleMapper() {
		return new ObjectMapper();
	}

	@Bean
	public TenantProvider tenantProvider() {
		return TenantContextHolder::getCurrentTenantId;
	}


	@Bean
	public Sender gcmSender() {
		return new Sender(gcmKey);
	}

	@Bean
	public MobileNotificationSender appleNS(APNSClient client) {
		return new MobileNotificationSender(client, 8999);
	}

	@Bean
	public MobileNotificationSender androidNS(GCMClient client) {
		return new MobileNotificationSender(client, 1000);
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

	@Bean
	public AmazonCloudService amazonCloudService() {
		return new AmazonCloudService(accessKey, accessSecretKey, cloudfrontUrl, bucketName);
	}

}