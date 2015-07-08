package com.wordrails.api;

import java.util.Set;

import javax.persistence.Entity;
import javax.validation.Validator;

import org.reflections.Reflections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.core.event.ValidatingRepositoryEventListener;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.MediaType;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class WordRailsConfiguration extends RepositoryRestMvcConfiguration {
	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.setDefaultMediaType(MediaType.APPLICATION_JSON);
		
		Reflections reflections = new Reflections("com.wordrails.business");
		Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
		config.exposeIdsFor(entities.toArray(new Class<?>[0]));
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