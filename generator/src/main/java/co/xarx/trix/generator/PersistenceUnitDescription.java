package co.xarx.trix.generator;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.generator.domain.AbstractField;
import co.xarx.trix.generator.domain.JavaField;
import co.xarx.trix.generator.domain.TrixField;
import co.xarx.trix.generator.scope.EntityDescription;
import co.xarx.trix.generator.scope.FieldDescription;
import co.xarx.trix.generator.scope.QueryDescription;
import co.xarx.trix.persistence.TrixRepository;
import com.google.common.collect.Lists;
import org.reflections.Reflections;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class PersistenceUnitDescription {

	private static final String JAVA_LANG = "java.lang.";

	public List<EntityDescription> entities;
	private final Map<Class<?>, Class<? extends JpaRepository>> entityRepository;
	private final Map<Class<?>, EntityDescription> entityMap;

	public PersistenceUnitDescription(String prefix) {
		entities = new ArrayList<>();

		Reflections reflections = new Reflections(prefix);
		Set<Class<? extends JpaRepository>> classes = getRepositoryClasses(reflections, JpaRepository.class);

		entityRepository = new HashMap<>();
		Map<Class<?>, List<QueryDescription>> entityQueries = getQueriesFromRepositories(classes);

		entityMap = extractEntities(reflections, entityQueries);

		entities.addAll(entityMap.values());
		sortEntitiesByName();
		insertEntitiesIntoFields();

		Set<Class<?>> projectionClasses = reflections.getTypesAnnotatedWith(Projection.class);
		extractProjections(projectionClasses);
	}

	public <T> Set<Class<? extends T>> getRepositoryClasses(Reflections reflections, Class<T> extendsFrom) {
		Set<Class<? extends T>> classes = reflections.getSubTypesOf(extendsFrom);
		Set<Class<? extends T>> classes2 = new HashSet(classes);
		for (Class<? extends T> c : classes) {
			List<Annotation> annotations = Lists.newArrayList(c.getAnnotations());
			for (Annotation annotation : annotations) {
				if(annotation.annotationType().equals(NoRepositoryBean.class)) {
					classes2.remove(c);
				} else if(annotation.annotationType().equals(RepositoryRestResource.class)
						&& !((RepositoryRestResource)annotation).exported()) {
					classes2.remove(c);
				}
			}
		}
		classes = classes2;
		return classes;
	}

	public Map<Class<?>, List<QueryDescription>> getQueriesFromRepositories(Set<Class<? extends JpaRepository>> classes) {
		Map<Class<?>, List<QueryDescription>> queriesMap = new HashMap<>();
		for (Class<? extends JpaRepository> repositoryClass : classes) {
			RepositoryRestResource repositoryResource = repositoryClass.getAnnotation(RepositoryRestResource.class);
			SdkExclude repositoryIgnore = repositoryClass.getAnnotation(SdkExclude.class);
			if (repositoryIgnore == null && (repositoryResource == null || repositoryResource.exported())) {
				queriesMap = getRepositoryQueries(repositoryClass);
			}
		}

		return queriesMap;
	}

	public Map<Class<?>, List<QueryDescription>> getRepositoryQueries(Class<? extends JpaRepository> repositoryClass) {
		Map<Class<?>, List<QueryDescription>> queriesMap = new HashMap<>();
		Type[] interfaces = repositoryClass.getGenericInterfaces();
		for (Type genericInterface : interfaces) {
			if (genericInterface instanceof ParameterizedType) {
				ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
				Type rawType = parameterizedType.getRawType();
				if (JpaRepository.class.equals(rawType) || TrixRepository.class.equals(rawType)) {
					Class<?> entity = (Class<?>) parameterizedType.getActualTypeArguments()[0];
					entityRepository.put(entity, repositoryClass);
					List<QueryDescription> queries = getRepositoryQueries(repositoryClass, entity);
					queriesMap.put(entity, queries);
				}
			}
		}

		return queriesMap;
	}

	public Map<Class<?>, EntityDescription> extractEntities(Reflections reflections, Map<Class<?>, List<QueryDescription>> entityQueries) {
		Map<Class<?>, EntityDescription> entityMap = new HashMap<>();
		Set<Class<?>> entityClassSet = getEntityClasses(reflections);

		for (Class<?> entityClass : entityClassSet) {
			Class<?> repositoryClass = entityRepository.get(entityClass);
			if (repositoryClass != null) {
				EntityDescription entity = getEntityDescription(entityClass, repositoryClass, entityQueries.get(entityClass));
				List<AbstractField> fields = getFieldsFromEntity(entityClass);

				for (AbstractField field : fields) {
					FieldDescription fd = new FieldDescription();
					fd.type = field.getTypeName();
					fd.name = field.getName();
					fd.nameUppercase = field.getNameUppercase();
					if (field.isId()) {
						entity.id = fd;
					} else if (field.isList()) {
						Class<?> genericType = field.getGenericType();
						String genericName = genericType.getSimpleName();
						if(field.isSimpleType(genericType))
							genericName = genericType.getName();

						if (field.isMap()) {
							fd.type = field.getParametrizedGenericTypeName();
							fd.entity = new EntityDescription();
							fd.entity.name = genericType.getSimpleName();
							entity.fields.add(fd);
						} else {
							if (field.isIncludeAsReference()) {
								fd.type = field.getTypeName()
										+ "<" + (field.isSimpleType(genericType) ? genericName : "java.lang.String") + ">";
								fd.entity = new EntityDescription();
								fd.entity.name = genericType.getSimpleName();
								entity.fields.add(fd);
							}

							FieldDescription relationship = new FieldDescription();
							relationship.type = "java.util.List<" + genericName + (field.isSimpleType(genericType) ? "" : "Dto") + ">";
							relationship.name = field.getName();
							relationship.nameUppercase = field.getNameUppercase();
							relationship.entity = new EntityDescription();
							relationship.entity.name = genericType.getSimpleName();
							relationship.collection = true;
							relationship.mappedBy = field.isMappedBy();
							relationship.collectionType = List.class.getName();
							relationship.elementType = genericName;
							entity.relationships.add(relationship);
						}
					} else {
						if (field.isRelationship()) {
							FieldDescription relationship = new FieldDescription();
							relationship.type = field.getTypeSimpleName()
									+ (!field.isSimpleType(field.getType()) ? "Dto" : "");
							relationship.name = fd.name;
							relationship.nameUppercase = fd.nameUppercase;
							relationship.entity = new EntityDescription();
							relationship.entity.name = field.getTypeSimpleName();
							entity.relationships.add(relationship);
							if (field.isIncludeAsReference()) {
								fd.type = "java.lang.String";
								fd.entity = new EntityDescription();
								fd.entity.name = field.getTypeSimpleName();
								entity.fields.add(fd);
							}
						} else {
							entity.fields.add(fd);
						}
					}

				}
				entityMap.put(entityClass, entity);
			}
		}

		return entityMap;
	}

	public EntityDescription getEntityDescription(Class<?> entityClass, Class<?> repositoryClass,
												  List<QueryDescription> queries) {
		EntityDescription entity = new EntityDescription();
		entity.fullName = entityClass.getName();
		entity.name = entityClass.getSimpleName();
		entity.nameLowercase = getLowercase(entity.name);
		entity.repositoryFullName = repositoryClass.getName();
		entity.repositoryName = repositoryClass.getSimpleName();
		entity.queries = queries;

		String plural = getPlural(entity.name);
		entity.plural = plural;
		entity.pluralLowercase = getLowercase(plural);
		return entity;
	}

	public List<AbstractField> getFieldsFromEntity(Class<?> entityClass) {
		List<AbstractField> fields = new ArrayList<>();
		try {
			PropertyDescriptor[] propertyDescriptors =
					Introspector.getBeanInfo(entityClass, BaseEntity.class).getPropertyDescriptors();
			for (PropertyDescriptor pd : propertyDescriptors) {

				if(pd.getReadMethod() != null) {
					try {
						Field f = entityClass.getDeclaredField(pd.getName());

						if (!Modifier.isStatic(f.getModifiers())
								&& !f.isAnnotationPresent(Transient.class)
								&& !f.isAnnotationPresent(SdkExclude.class)) {
							JavaField jf = new JavaField(f);
							fields.add(jf);
						}
					} catch (NoSuchFieldException e) {
						SdkInclude sdkInclude = pd.getReadMethod().getAnnotation(SdkInclude.class);
						if (sdkInclude != null) {
							TrixField tf = getTrixField(pd, sdkInclude.asReference());
							fields.add(tf);
						}
					}
				}
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return fields;
	}

	public Set<Class<?>> getEntityClasses(Reflections reflections) {
		Set<Class<?>> entityClassSet = reflections.getTypesAnnotatedWith(Entity.class);

		Set<Class<?>> entityClassSet2 = new HashSet(entityClassSet);
		entityClassSet.stream()
				.filter(c -> AnnotationUtils.isAnnotationDeclaredLocally(SdkExclude.class, c))
				.forEach(entityClassSet2::remove);
		entityClassSet = entityClassSet2;
		return entityClassSet;
	}

	public void insertEntitiesIntoFields() {
		for (EntityDescription entity : entities) {
			List<FieldDescription> fields = new ArrayList<>();
			fields.addAll(entity.fields);
			fields.addAll(entity.relationships);
			fields.stream()
					.filter(field -> field.entity != null)
					.forEach(this::insertEntityIntoField);
		}
	}

	public void insertEntityIntoField(FieldDescription field) {
		entities.stream()
				.filter(entity2 -> field.entity.name.equals(entity2.name))
				.forEach(entity2 -> field.entity = entity2);
	}

	public void sortEntitiesByName() {
		Collections.sort(entities, (entity1, entity2) -> entity1.name.compareTo(entity2.name));
	}

	public List<QueryDescription> getRepositoryQueries(Class<? extends JpaRepository> repositoryClass, Class<?> entity) {
		List<QueryDescription> queries = new ArrayList<>();
		Method[] methods = repositoryClass.getDeclaredMethods();
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

	public QueryDescription getQueryDescription(Method method, String path) {
		QueryDescription query = new QueryDescription();
		query.name = path;
		query.nameUppercase = getNameUppercase(path);
		query.collection = Collection.class.isAssignableFrom(method.getReturnType());

		Annotation[][] annotations = method.getParameterAnnotations();
		Type[] types = method.getGenericParameterTypes();
		for (int i = 0; i < types.length; ++i) {
			if (Pageable.class.equals(types[i])) {
				FieldDescription page = new FieldDescription();
				page.name = "page";
				page.type = getType(Integer.class);
				query.parameters.add(page);

				FieldDescription size = new FieldDescription();
				size.name = "size";
				size.type = getType(Integer.class);
				query.parameters.add(size);

				FieldDescription sort = new FieldDescription();
				sort.name = "sort";
				sort.type = "List<String>";
				query.parameters.add(sort);

			} else {
				FieldDescription parameter = new FieldDescription();
				parameter.type = getType(types[i]);
				for (int j = 0; j < annotations[i].length; ++j) {
					if (annotations[i][j] instanceof Param) {
						Param param = (Param) annotations[i][j];
						parameter.name = param.value();
					}
				}
				query.parameters.add(parameter);
			}
		}
		return query;
	}

	public void extractProjections(Set<Class<?>> projectionClassSet) {
		for (Class<?> projectionClass : projectionClassSet) {
			Projection projectionAnnotation = projectionClass.getAnnotation(Projection.class);
			EntityDescription projection = getProjectionEntityDescription(projectionClass);
			for (Method method : projectionClass.getMethods()) {
				Class<?> collectionType;
				Class<?> returnType = method.getReturnType();
				if (Collection.class.isAssignableFrom(returnType)) {
					collectionType = List.class;
					ParameterizedType parameterizedType = (ParameterizedType) method.getGenericReturnType();
					Type[] typeArguments = parameterizedType.getActualTypeArguments();
					Type typeArgument = typeArguments[0];
					if (typeArgument instanceof Class<?>) {
						returnType = (Class<?>) typeArgument;
					}
				} else {
					collectionType = null;
				}
				String type = returnType.getName();
				if (returnType.isAnnotationPresent(Entity.class) || returnType.isAnnotationPresent(Projection.class)) {
					type = returnType.getSimpleName() + (!isSimpleType(returnType) ? "Dto" : "");
				}
				if (collectionType != null) {
					type = collectionType.getName() + "<" + returnType.getSimpleName() + (!isSimpleType(returnType) ? "Dto" : "") + ">";
				}
				FieldDescription fieldDescription = new FieldDescription();
				fieldDescription.type = type;
				fieldDescription.name = method.getName();
				if (fieldDescription.name.startsWith("get")) {
					fieldDescription.name = getLowercase(fieldDescription.name.substring("get".length()));
				}
				projection.fields.add(fieldDescription);
			}
			Class<?>[] entityClasses = projectionAnnotation.types();
			for (Class<?> entityClass : entityClasses) {
				EntityDescription entity = entityMap.get(entityClass);
				if (entity == null) {
					System.out.println("Entity null for " + entityClass.getSimpleName());
				} else if (entity.projections == null) {
					System.out.println("Entity projections null for " + entityClass.getSimpleName());
				} else if (projection == null) {
					System.out.println("Projection null for " + entityClass.getSimpleName());
				}
				entity.projections.add(projection);
			}
		}
	}

	public EntityDescription getProjectionEntityDescription(Class<?> projectionClass) {
		EntityDescription projection = new EntityDescription();
		projection.fullName = projectionClass.getName();
		projection.name = projectionClass.getSimpleName();
		projection.nameLowercase = getLowercase(projection.name);
		projection.plural = getPlural(projection.name);
		projection.pluralLowercase = getLowercase(projection.plural);
		return projection;
	}

	private TrixField getTrixField(PropertyDescriptor pd, boolean asReference) {
		TrixField t = new TrixField();

		Method readMethod = pd.getReadMethod();
		Class<?> returnType = readMethod.getReturnType();
		Type genericReturnType = readMethod.getGenericReturnType();

		t.setType(returnType);
		if (genericReturnType != null) {
			t.setGenericType(genericReturnType.getClass());
			t.setParametrizedGenericTypeName(genericReturnType.toString());
		}

		t.setMap(returnType.isAssignableFrom(Collection.class));
		t.setMap(returnType.isAssignableFrom(Map.class));
		t.setIncludeAsReference(asReference);
		t.setName(pd.getName());

		return t;
	}

	private String getPlural(String noun) {
		if (noun.endsWith("y")) {
			noun = noun.substring(0, noun.length() - 1);
			noun = noun + "ies";
		} else if (noun.endsWith("s")) {
			// Placeholder
		} else {
			noun = noun + "s";
		}
		return noun;
	}

	private String getLowercase(String name) {
		return name.substring(0, 1).toLowerCase() + name.substring(1);
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

	private static boolean isSimpleType(Class<?> clazz){
		return getWrapperTypes().contains(clazz) || clazz.equals(String.class);
	}
	private static Set<Class<?>> getWrapperTypes(){
		Set<Class<?>> ret = new HashSet<>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Void.class);
		return ret;
	}
}