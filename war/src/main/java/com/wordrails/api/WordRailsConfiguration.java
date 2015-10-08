package com.wordrails.api;

import com.wordrails.elasticsearch.ElasticSearchService;
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
//@EnableRedisHttpSession
public class WordRailsConfiguration extends RepositoryRestMvcConfiguration {

	@Value("${elasticsearch.host}")
	private String host;
	@Value("${elasticsearch.port}")
	private Integer port;

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
		return new ElasticSearchService(host, port);
	}


//	@Bean
//	public JedisConnectionFactory connectionFactory() {
//		return new JedisConnectionFactory();
//	}

//	@Bean
//    public Validator validator() {
//        return new LocalValidatorFactoryBean();
//    }
//
//    @Bean
////    @Override
//    public ValidatingRepositoryEventListener validatingRepositoryEventListener() {
//        ValidatingRepositoryEventListener listener = new ValidatingRepositoryEventListener();
//        configureValidatingRepositoryEventListener(listener);
//        listener.addValidator("afterCreate", validator());
//        listener.addValidator("beforeCreate", validator());
//        return listener;
//    }
}