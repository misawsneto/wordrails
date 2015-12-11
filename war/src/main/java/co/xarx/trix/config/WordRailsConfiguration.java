package co.xarx.trix.config;

import org.reflections.Reflections;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.MediaType;

import javax.persistence.Entity;
import java.util.Set;

//@Configuration
public class WordRailsConfiguration extends RepositoryRestMvcConfiguration {

//	@Value("${elasticsearch.host:'localhost'}")
//	private String eshost;
//	@Value("${elasticsearch.port:9300}")
//	private Integer esport;

	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.setDefaultMediaType(MediaType.APPLICATION_JSON);

		Reflections reflections = new Reflections("co.xarx.trix.domain");
		Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
		config.exposeIdsFor(entities.toArray(new Class<?>[0]));
	}

//	@Bean
//	@PostConstruct
//	public ElasticSearchService elasticsearchService() {
//		return new ElasticSearchService(eshost, esport);
//	}

}