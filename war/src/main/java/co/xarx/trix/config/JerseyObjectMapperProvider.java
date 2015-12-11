package co.xarx.trix.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import org.springframework.stereotype.Component;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

@Provider
@Component
public class JerseyObjectMapperProvider implements ContextResolver<ObjectMapper> {

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return getObjectMapper();
	}

	public static ObjectMapper getObjectMapper() {
		ObjectMapper result = new ObjectMapper();
		result.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		result.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		Hibernate4Module hbModule = new Hibernate4Module();
		hbModule.configure(Hibernate4Module.Feature.FORCE_LAZY_LOADING, false);
		hbModule.configure(Hibernate4Module.Feature.SERIALIZE_IDENTIFIER_FOR_LAZY_NOT_LOADED_OBJECTS, true);
		hbModule.configure(Hibernate4Module.Feature.USE_TRANSIENT_ANNOTATION, false);
		result.registerModule(hbModule);
		return result;
	}
}