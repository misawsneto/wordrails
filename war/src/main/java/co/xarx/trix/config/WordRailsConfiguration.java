package co.xarx.trix.config;

import co.xarx.trix.services.ElasticSearchService;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.MediaType;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import java.util.Set;

@Configuration
public class WordRailsConfiguration extends RepositoryRestMvcConfiguration {

	@Value("${elasticsearch.host:'localhost'}")
	private String esHost;
	@Value("${elasticsearch.port:9300}")
	private Integer esPort;
	@Value("${elasticsearch.username:trix_admin}")
	private String esUser;
	@Value("${elasticsearch.password:tr1xsearch}")
	private String esPassword;

	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.setDefaultMediaType(MediaType.APPLICATION_JSON);

		Reflections reflections = new Reflections("co.xarx.trix.domain");
		Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
		config.exposeIdsFor(entities.toArray(new Class<?>[0]));
	}

	@Bean
	@PostConstruct
	public ElasticSearchService elasticsearchService() {
		return new ElasticSearchService(esHost, esPort, esUser, esPassword);
	}

//	@Bean
//	public MessageListenerAdapter messageListener() {
//		return new MessageListenerAdapter( new WordrailsMessageListener() );
//	}
//
//	@Bean
//	public RedisMessageListenerContainer redisContainer(JedisConnectionFactory jcf) {
//		RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//		container.setTaskExecutor(taskExecutor());
//		container.setConnectionFactory(jcf);
//		container.addMessageListener(messageListener(), topic());
//		return container;
//	}
//
//	@Bean(name = "redisTaskExecutor")
//	public TaskExecutor taskExecutor() {
//		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		taskExecutor.setQueueCapacity(1);
//		taskExecutor.setCorePoolSize(3);
//		return taskExecutor;
//	}
//
//	@Bean(name = "applicationChannel")
//	public Topic topic() {
//		return new ChannelTopic("pubsub:application");
//	}
//
//	@Bean
//	public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jcf) throws Exception {
//		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
//		redisTemplate.setConnectionFactory(jcf);
//		return redisTemplate;
//	}

}