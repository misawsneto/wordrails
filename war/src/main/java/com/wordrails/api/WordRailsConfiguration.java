package com.wordrails.api;

import java.util.Set;

import javax.annotation.PostConstruct;
import javax.persistence.Entity;

import com.wordrails.elasticsearch.ElasticsearchService;
import com.wordrails.elasticsearch.PostEsRepository;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.MediaType;

@Configuration
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
	public ElasticsearchService elasticsearchService(){
		return new ElasticsearchService(host, port);
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