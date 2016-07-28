package co.xarx.trix;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

@ApplicationPath("/api")
public class ResteasyApplication extends Application {

//	private static final String PACKAGE = "co.xarx.trix.web.rest";
//
//	private final Set<Class<?>> classes;
//
//	public ResteasyApplication() {
//		Reflections reflections = new Reflections(PACKAGE);
//		classes = reflections.getSubTypesOf(AbstractResource.class).stream().collect(Collectors.toSet());
//	}
//
//	@Override
//	public Set<Class<?>> getClasses() {
//		return classes;
//	}
}
