package co.xarx.trix.config;

import org.reflections.Reflections;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.http.MediaType;

import javax.persistence.Entity;
import java.util.Set;

@Configuration
public class RestConfig extends RepositoryRestMvcConfiguration {

	@Override
	protected void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
		config.setDefaultMediaType(MediaType.APPLICATION_JSON);
		config.setReturnBodyOnCreate(true);
		config.setReturnBodyOnUpdate(true);

		Reflections reflections = new Reflections("co.xarx.trix.domain");
		Set<Class<?>> entities = reflections.getTypesAnnotatedWith(Entity.class);
		config.exposeIdsFor(entities.toArray(new Class<?>[0]));
	}
}