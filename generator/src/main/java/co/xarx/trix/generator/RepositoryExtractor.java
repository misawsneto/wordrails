package co.xarx.trix.generator;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.generator.scope.FieldDescription;
import co.xarx.trix.generator.scope.QueryDescription;
import com.google.common.collect.Lists;
import org.apache.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class RepositoryExtractor {

	Logger log = Logger.getLogger(this.getClass().getName());

	private static final String JAVA_LANG = "java.lang.";

	private Map<Class<?>, List<QueryDescription>> repositoryQueries;
	private Map<Class<?>, Class<?>> repositoryEntities;

	public RepositoryExtractor(Set repositoryClasses) {
		repositoryQueries = new HashMap<>();
		repositoryEntities = new HashMap<>();

		Set<Class> classes = excludeIgnoredRepositories(repositoryClasses);

		log.info("Generating SDK for the following repositories:");
		for (Class<?> aClass : classes) {
			log.info("\t" + aClass.getSimpleName());
		}

		for (Class<?> repositoryClass : classes) {
			repositoryQueries.putAll(getQueriesFromRepository(repositoryClass));
		}
	}

	public Map<Class<?>, List<QueryDescription>> getRepositoryQueries() {
		return repositoryQueries;
	}

	public Map<Class<?>, Class<?>> getRepositoryEntities() {
		return repositoryEntities;
	}

	private Set<Class> excludeIgnoredRepositories(Set<Class> classes) {
		Set<Class> classes2 = new HashSet(classes);
		classes.stream().filter(this::isIgnored).forEach(classes2::remove);
		classes = classes2;
		return classes;
	}

	public boolean isIgnored(Class c) {
		boolean isIgnored = false;

		List<Annotation> annotations = Lists.newArrayList(c.getAnnotations());
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(NoRepositoryBean.class)) {
				return true;
			} else if (annotation.annotationType().equals(RepositoryRestResource.class)) {
				boolean isNotExported = !((RepositoryRestResource) annotation).exported();
				return isNotExported;
			}
		}

		Class[] interfaces = c.getInterfaces();
		for (Class anInterface : interfaces) {
			if(isIgnored(anInterface))
				return true;
		}

		return isIgnored;
	}

	private Map<Class<?>, List<QueryDescription>> getQueriesFromRepository(Class<?> repositoryClass) {
		Map<Class<?>, List<QueryDescription>> queriesMap = new HashMap<>();
		Type[] interfaces = repositoryClass.getGenericInterfaces();
		for (Type genericInterface : interfaces) {
			if (genericInterface instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
				Class<?> entity = (Class<?>) parameterizedType.getActualTypeArguments()[0];
				Type rawType = parameterizedType.getRawType();
				if (JpaRepository.class.equals(rawType)) {
					repositoryEntities.put(entity, repositoryClass);
					List<QueryDescription> queries = getQueriesFromMethods(repositoryClass.getDeclaredMethods());
					queriesMap.put(entity, queries);
				}
			}
		}

		return queriesMap;
	}

	private List<QueryDescription> getQueriesFromMethods(Method[] methods) {
		List<QueryDescription> queries = new ArrayList<>();
		if (methods.length > 0) {
			for (Method method : methods) {
				RestResource methodResource = method.getAnnotation(RestResource.class);
				SdkExclude methodIgnore = method.getAnnotation(SdkExclude.class);
				if (methodIgnore == null && (methodResource == null || methodResource.exported())) {
					String path = method.getName();
					if (methodResource != null && !"".equals(methodResource.path())) {
						path = methodResource.path();
					}
					QueryDescription query = getQueryDescription(method, path);
					queries.add(query);
				}
			}
		}
		return queries;
	}

	private QueryDescription getQueryDescription(Method method, String path) {
		QueryDescription query = new QueryDescription();
		query.name = path;
		query.nameUppercase = getNameUppercase(path);
		query.collection = Collection.class.isAssignableFrom(method.getReturnType());

		Annotation[][] annotations = method.getParameterAnnotations();
		Type[] types = method.getGenericParameterTypes();

		Map<Type, Annotation[]> annotatedTypes = new HashMap<>();
		Map<Type, Annotation[]> pageableTypes = new HashMap<>();
		for (int i = 0; i < types.length; ++i) {
			if (Pageable.class.equals(types[i])) {
				pageableTypes.put(types[i], annotations[i]);
			} else {
				annotatedTypes.put(types[i], annotations[i]);
			}
		}

		annotatedTypes.putAll(pageableTypes); //ensures that pageable is always last

		if("findPersons".equals(method.getName())){
			String.valueOf(true);
		}

		query.parameters = getQueryParameters(annotatedTypes);

		return query;
	}

	private List<FieldDescription> getQueryParameters(Map<Type, Annotation[]> annotatedTypes) {
		List<FieldDescription> parameters = new ArrayList<>();

		for (Type type : annotatedTypes.keySet()) {
			Annotation[] annotationz = annotatedTypes.get(type);
			if (!Pageable.class.equals(type)) {
				FieldDescription parameter = new FieldDescription();
				parameter.type = getType(type);
				for (Annotation annotation : annotationz) {
					if (annotation instanceof Param) {
						parameter.name = ((Param) annotation).value();
						break;
					}
				}
				parameters.add(parameter);
			}
		}

		for (Type type : annotatedTypes.keySet()) {
			if (Pageable.class.equals(type)) {
				FieldDescription page = new FieldDescription();
				page.name = "page";
				page.type = getType(Integer.class);
				parameters.add(page);

				FieldDescription size = new FieldDescription();
				size.name = "size";
				size.type = getType(Integer.class);
				parameters.add(size);

				FieldDescription sort = new FieldDescription();
				sort.name = "sort";
				sort.type = "List<String>";
				parameters.add(sort);
			}
		}

		return parameters;
	}

	private String getType(Type type) {
		if (type instanceof Class) {
			Class<?> _class = (Class<?>) type;
			return getType(_class);
		} else {
			return type.toString();
		}
	}

	private String getNameUppercase(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	private String getType(Class<?> type) {
		String simpleName;
		String name = type.getName();
		if (name.startsWith(JAVA_LANG)) {
			simpleName = name.substring(JAVA_LANG.length());
		} else {
			simpleName = name;
		}
		return simpleName;
	}
}