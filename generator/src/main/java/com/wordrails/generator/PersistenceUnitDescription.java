package com.wordrails.generator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.reflections.Reflections;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.data.rest.core.config.Projection;

public class PersistenceUnitDescription {
	private static final String ENCODING = "UTF-8";
	private static final String JAVA_LANG = "java.lang.";
	
	public List<EntityDescription> entities;
	
	public PersistenceUnitDescription(String prefix) {
		Reflections reflections = new Reflections(prefix);

		Map<Class<?>, Class<? extends JpaRepository>> entityRepository = new HashMap<>(); 
		Map<Class<?>, List<QueryDescription>> entityQueries = new HashMap<>();
		Set<Class<? extends JpaRepository>> classes = reflections.getSubTypesOf(JpaRepository.class);
		for (Class<? extends JpaRepository> repositoryClass : classes) {
			RepositoryRestResource repositoryResource = repositoryClass.getAnnotation(RepositoryRestResource.class);
			if (repositoryResource == null || repositoryResource.exported()) {
				Type[] interfaces = repositoryClass.getGenericInterfaces();
				for (Type genericInterface : interfaces) {
					if (genericInterface instanceof ParameterizedType) {
						ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
						Type rawType = parameterizedType.getRawType();
						if (JpaRepository.class.equals(rawType)) {
							Class<?> entity = (Class<?>) parameterizedType.getActualTypeArguments()[0];
							entityRepository.put(entity, repositoryClass);
							List<QueryDescription> queries = new ArrayList<>();
							entityQueries.put(entity, queries);
							Method[] methods = repositoryClass.getDeclaredMethods();
							if (methods.length > 0) {
								for (Method method : methods) {
									RestResource methodResource = method.getAnnotation(RestResource.class);
									if (methodResource == null || methodResource.exported()) {
										QueryDescription query = new QueryDescription();
										query.collection = Collection.class.isAssignableFrom(method.getReturnType());										
										if (methodResource == null || "".equals(methodResource.path())) {
											query.name = method.getName();
											query.nameUppercase = getNameUppercase(query.name);
 										} else {
 											query.name = methodResource.path();
 											query.nameUppercase = getNameUppercase(query.name);
 										}
										
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
										queries.add(query);
									}
								}
							}
						}
					}
				}
			}
		}

		Map<Class<?>, EntityDescription> entityMap = new HashMap<Class<?>, EntityDescription>();
		Set<Class<?>> entityClassSet = reflections.getTypesAnnotatedWith(Entity.class);
		for (Class<?> entityClass : entityClassSet) {
			Class<?> repositoryClass = entityRepository.get(entityClass);
			if (repositoryClass != null) {
				EntityDescription entity = new EntityDescription();
				entity.fullName = entityClass.getName();
				entity.name = entityClass.getSimpleName();
				entity.nameLowercase = getLowercase(entity.name);				
				entity.repositoryFullName = repositoryClass.getName();
				entity.repositoryName = repositoryClass.getSimpleName();
				entity.queries = entityQueries.get(entityClass);
	
				String plural = getPlural(entity.name);
				entity.plural = plural;
				entity.pluralLowercase = getLowercase(plural);
	
				Field[] fields = entityClass.getFields();
				for (Field field : fields) {
					if (!Modifier.isStatic(field.getModifiers()) && !field.isAnnotationPresent(Transient.class)) {
						if (field.isAnnotationPresent(Id.class)) {
							FieldDescription id = new FieldDescription();
							id.type = getType(field);
							id.name = field.getName();
							id.nameUppercase = getNameUppercase(field);
							entity.id = id;
						} else if (field.isAnnotationPresent(OneToMany.class) || field.isAnnotationPresent(ManyToMany.class)) {
							Type genericType = field.getGenericType();
							ParameterizedType parameterizedGenericType = (ParameterizedType) genericType;
							Type[] actualTypeArguments = parameterizedGenericType.getActualTypeArguments();
							Type actualTypeArgument = actualTypeArguments[0];
							Class<?> type = (Class<?>) actualTypeArgument;
							
							if (field.isAnnotationPresent(NotNull.class)) {
								Size size = field.getAnnotation(Size.class);
								if (size != null && size.min() > 0) {
									FieldDescription fieldDescription = new FieldDescription();
									fieldDescription.type = field.getType().getName() + "<String>";
									fieldDescription.name = field.getName();
									fieldDescription.nameUppercase = getNameUppercase(field);
									fieldDescription.entity = new EntityDescription();
									fieldDescription.entity.name = type.getSimpleName();
									entity.fields.add(fieldDescription);
								}
							}

							FieldDescription relationship = new FieldDescription();														
							relationship.type = "List<" + type.getSimpleName() + "Dto>";						
							relationship.name = field.getName();
							relationship.nameUppercase = getNameUppercase(field);
							relationship.entity = new EntityDescription();
							relationship.entity.name = type.getSimpleName();
							relationship.collection = true;
							relationship.mappedBy = (field.isAnnotationPresent(OneToMany.class) && !"".equals(field.getAnnotation(OneToMany.class).mappedBy())) 
														|| (field.isAnnotationPresent(ManyToMany.class) && !"".equals(field.getAnnotation(ManyToMany.class).mappedBy()));
//							relationship.collectionType = field.getType().getName();
							relationship.collectionType = List.class.getName();
							relationship.elementType = type.getSimpleName();
							entity.relationships.add(relationship);
						} else {
							if (field.isAnnotationPresent(ManyToOne.class)
									|| field.isAnnotationPresent(OneToOne.class)) {
								FieldDescription relationshipDescription = new FieldDescription();
								relationshipDescription.type = field.getType().getSimpleName() + "Dto";
								relationshipDescription.name = field.getName();
								relationshipDescription.nameUppercase = getNameUppercase(field);
								relationshipDescription.entity = new EntityDescription();
								relationshipDescription.entity.name = field.getType().getSimpleName();								
								entity.relationships.add(relationshipDescription);							
								if (field.isAnnotationPresent(NotNull.class)) {
									FieldDescription fieldDescription = new FieldDescription();
									fieldDescription.type = "String";
									fieldDescription.name = field.getName();
									fieldDescription.nameUppercase = getNameUppercase(field);
									fieldDescription.entity = new EntityDescription();
									fieldDescription.entity.name = field.getType().getSimpleName();																		
									entity.fields.add(fieldDescription);
								}
							} else {
								FieldDescription fieldDescription = new FieldDescription();
								fieldDescription.type = getType(field);
								fieldDescription.name = field.getName();
								fieldDescription.nameUppercase = getNameUppercase(field);
								entity.fields.add(fieldDescription);
							}
						}
					}
				}
				entityMap.put(entityClass, entity);
			}
		}
		entities = new ArrayList<>();
		entities.addAll(entityMap.values());
		Collections.sort(entities, new Comparator<EntityDescription>() {
			@Override
			public int compare(EntityDescription entity1, EntityDescription entity2) {
				return entity1.name.compareTo(entity2.name);
			}
		});
		for (EntityDescription entity : entities) {
			List<FieldDescription> fields = new ArrayList<FieldDescription>();
			fields.addAll(entity.fields);
			fields.addAll(entity.relationships);
			for (FieldDescription field : fields) {
				if (field.entity != null) {
					for (EntityDescription entity2 : entities) {
						if (field.entity.name.equals(entity2.name)) {
							field.entity = entity2;
						}
					}
				}
			}
		}

		Set<Class<?>> projectionClassSet = reflections.getTypesAnnotatedWith(Projection.class);
		for (Class<?> projectionClass : projectionClassSet) {
			Projection projectionAnnotation = projectionClass.getAnnotation(Projection.class);
			EntityDescription projection = new EntityDescription();
			projection.fullName = projectionClass.getName();
			projection.name = projectionClass.getSimpleName();
			projection.nameLowercase = getLowercase(projection.name);
			projection.plural = getPlural(projection.name);
			projection.pluralLowercase = getLowercase(projection.plural);
			for (Method method : projectionClass.getMethods()) {
				Class<?> collectionType;
				Class<?> returnType = method.getReturnType();
				if (Collection.class.isAssignableFrom(returnType)) {
//					collectionType = returnType;
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
				if (returnType.isAnnotationPresent(Entity.class) 
						|| returnType.isAnnotationPresent(Projection.class)) {
					type = returnType.getSimpleName() + "Dto";
				}
				if (collectionType != null) {
					type = collectionType.getName() + "<" + returnType.getSimpleName() + "Dto>";
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
				entity.projections.add(projection);
			}
		}
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
		
	private String getType(Field field) {
		return getType(field.getType());
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
	
	private String getNameUppercase(Field field) {
		String name = field.getName();		
		return getNameUppercase(name);
	}
	
	private String getNameUppercase(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);		
	}
}