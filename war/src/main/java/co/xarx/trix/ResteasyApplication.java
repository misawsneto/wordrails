package co.xarx.trix;

import co.xarx.trix.web.rest.AbstractResource;
import org.reflections.Reflections;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationPath("/api")
public class ResteasyApplication extends Application {

//	@Override
//	public Map<String, Object> getProperties() {
//		return super.getProperties();
//	}
//
//	private static final String PACKAGE = "co.xarx.trix.web.rest";
//
//	private final Set<Class<?>> classes;
//
//	public ResteasyApplication() {
//		Reflections reflections = new Reflections(PACKAGE);
//		classes = reflections.getSubTypesOf(AbstractResource.class).stream().collect(Collectors.toSet());
//	}
//
	@Override
	public Set<Class<?>> getClasses() {
		Reflections reflections = new Reflections("co.xarx.trix.web.rest");
		return reflections.getSubTypesOf(AbstractResource.class).stream().collect(Collectors.toSet());
	}
}
