package com.wordrails.api;

import com.wordrails.elasticsearch.ElasticSearchService;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.MediaType;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;
import java.util.Set;

@Configuration
//@EnableRedisHttpSession
public class WordRailsConfiguration extends RepositoryRestMvcConfiguration {

	@Value("${elasticsearch.host:'localhost'}")
	private String eshost;
	@Value("${elasticsearch.port:9300}")
	private Integer esport;

	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.setDefaultMediaType(MediaType.APPLICATION_JSON);

		Reflections reflections = new Reflections("com.wordrails.business");
		Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
		config.exposeIdsFor(entities.toArray(new Class<?>[0]));
	}

	@Bean
	@PostConstruct
	public ElasticSearchService elasticsearchService() {
		return new ElasticSearchService(eshost, esport);
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