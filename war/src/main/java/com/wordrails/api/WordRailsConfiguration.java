package com.wordrails.api;

import java.util.Set;

import javax.persistence.Entity;

import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.MediaType;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class WordRailsConfiguration extends RepositoryRestMvcConfiguration {
	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.setDefaultMediaType(MediaType.APPLICATION_JSON);
		
		Reflections reflections = new Reflections("com.wordrails.business");
		Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
		config.exposeIdsFor(entities.toArray(new Class<?>[0]));
	}

	@Bean
	public JedisConnectionFactory connectionFactory() {
		return new JedisConnectionFactory();
	}
	
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