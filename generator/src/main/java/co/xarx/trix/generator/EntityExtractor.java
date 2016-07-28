package co.xarx.trix.generator;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import co.xarx.trix.domain.BaseEntity;
import co.xarx.trix.generator.domain.AbstractField;
import co.xarx.trix.generator.domain.JavaField;
import co.xarx.trix.generator.domain.TrixField;
import co.xarx.trix.generator.exception.InvalidEntityException;
import co.xarx.trix.generator.scope.EntityDescription;
import co.xarx.trix.generator.scope.FieldDescription;
import co.xarx.trix.generator.scope.QueryDescription;
import com.google.common.collect.Lists;
import org.reflections.Reflections;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.rest.core.annotation.RestResource;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class EntityExtractor {

	public Map<Class<?>, EntityDescription> extractEntities(Set<Class<?>> entityClasses,
															Map<Class<?>, List<QueryDescription>> entityQueries,
															Map<Class<?>, Class<?>> entityRepositories,
															Map<Class<?>, EntityDescription> projections) throws InvalidEntityException {
		Map<Class<?>, EntityDescription> result = new HashMap<>();

		for (Class<?> entityClass : entityClasses) {
			Class<?> repositoryClass = entityRepositories.get(entityClass);
			if (repositoryClass != null) {
				EntityDescription entity = getEntityDescription(entityClass, repositoryClass,
						entityQueries.get(entityClass), projections.get(entityClass));
				List<AbstractField> fields = getFieldsFromEntity(entityClass);

				if(fields.size() == 0)
					throw new InvalidEntityException("Entity " + entityClass.getSimpleName() + " has no fields");

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
						if (field.isSimpleType(genericType))
							genericName = genericType.getName();

						if (field.isMap()) {
							fd.type = field.getParameterizedGenericTypeName();
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

							FieldDescription relationship = getRelationshipFromField(field, genericType, genericName);
							entity.relationships.add(relationship);
						}
					} else {
						if (field.isRelationship()) {
							FieldDescription relationship = getRelationship(field, fd);
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
				result.put(entityClass, entity);
			}
		}

		return result;
	}

	public FieldDescription getRelationship(AbstractField field, FieldDescription fd) {
		FieldDescription relationship = new FieldDescription();
		relationship.type = field.getTypeSimpleName()
				+ (!field.isSimpleType(field.getType()) ? "Dto" : "");
		relationship.name = fd.name;
		relationship.nameUppercase = fd.nameUppercase;
		relationship.entity = new EntityDescription();
		relationship.entity.name = field.getTypeSimpleName();
		return relationship;
	}

	public FieldDescription getRelationshipFromField(AbstractField field, Class<?> genericType, String genericName) {
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
		return relationship;
	}

	public EntityDescription getEntityDescription(Class<?> entityClass,
												  Class<?> repositoryClass,
												  List<QueryDescription> queries,
												  EntityDescription projection) {
		EntityDescription entity = new EntityDescription();
		entity.fullName = entityClass.getName();
		entity.name = entityClass.getSimpleName();
		entity.nameLowercase = GeneratorUtil.getLowercase(entity.name);
		entity.repositoryFullName = repositoryClass.getName();
		entity.repositoryName = repositoryClass.getSimpleName();
		entity.queries = queries;
		if (projection != null) {
			entity.projections.add(projection);
		}

		String plural = GeneratorUtil.getPlural(entity.name);
		entity.plural = plural;
		entity.pluralLowercase = GeneratorUtil.getLowercase(plural);
		return entity;
	}

	public List<AbstractField> getFieldsFromEntity(Class<?> entityClass) {
		List<AbstractField> fields = new ArrayList<>();
		try {
			PropertyDescriptor[] propertyDescriptors =
					Introspector.getBeanInfo(entityClass, BaseEntity.class).getPropertyDescriptors();
			for (PropertyDescriptor pd : propertyDescriptors) {

				if (pd.getReadMethod() != null) {
					try {
						Field f = entityClass.getDeclaredField(pd.getName());
						boolean shouldInclude = shouldIncludeField(f);

						if (shouldInclude) {
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
			if(entityClass.getSuperclass()!=null && !BaseEntity.class.equals(entityClass.getSuperclass())){ // we
				// don't
				// want to process Object.class
				// do something with current's fields
				fields.addAll(getFieldsFromEntity(entityClass.getSuperclass()));
			}
		} catch (IntrospectionException e) {
			e.printStackTrace();
		}
		return fields;
	}

	public boolean shouldIncludeField(Field f) {
		if(Modifier.isStatic(f.getModifiers()))
			return false;

		List<Annotation> annotations = Lists.newArrayList(f.getAnnotations());
		for (Annotation annotation : annotations) {
			if (annotation.annotationType().equals(Transient.class)
					|| annotation.annotationType().equals(SdkExclude.class)) {
				return false;
			} else if (annotation.annotationType().equals(RestResource.class)) {
				boolean isExported = ((RestResource) annotation).exported();
				return isExported;
			}
		}
		return true;
	}

	public Set<Class<?>> getEntityClasses(String packageToScan) {
		Reflections reflections = new Reflections(packageToScan);
		Set<Class<?>> entityClasses = reflections.getTypesAnnotatedWith(Entity.class);
		Set<Class<?>> entityClassSet2 = new HashSet(entityClasses);
		entityClasses.stream()
				.filter(c -> AnnotationUtils.isAnnotationDeclaredLocally(SdkExclude.class, c))
				.forEach(entityClassSet2::remove);
		entityClasses = entityClassSet2;
		return entityClasses;
	}

	public void insertEntitiesIntoFields(List<EntityDescription> entities) {
		for (EntityDescription entity : entities) {
			List<FieldDescription> fields = new ArrayList<>();
			fields.addAll(entity.fields);
			fields.addAll(entity.relationships);
			fields.stream()
					.filter(field -> field.entity != null)
					.forEach(fieldDescription -> insertEntityIntoField(fieldDescription, entities));
		}
	}

	public void insertEntityIntoField(FieldDescription field, List<EntityDescription> entities) {
		entities.stream()
				.filter(entity2 -> field.entity.name.equals(entity2.name))
				.forEach(entity2 -> field.entity = entity2);
	}

	public void sortEntitiesByName(List<EntityDescription> entities) {
		Collections.sort(entities, (entity1, entity2) -> entity1.name.compareTo(entity2.name));
	}

	TrixField getTrixField(PropertyDescriptor pd, boolean asReference) {
		TrixField t = new TrixField();

		Method readMethod = pd.getReadMethod();
		Class<?> returnType = readMethod.getReturnType();
		Type genericReturnType = readMethod.getGenericReturnType();

		t.setMap(returnType.isAssignableFrom(Collection.class));
		t.setMap(returnType.isAssignableFrom(Map.class));
		t.setList(returnType.isAssignableFrom(List.class));
		t.setIncludeAsReference(asReference);
		t.setName(pd.getName());

		t.setType(returnType);
		if (genericReturnType != null) {
			if(genericReturnType.getClass().equals(ParameterizedTypeImpl.class))
				t.setGenericType((Class<?>) ((ParameterizedTypeImpl)genericReturnType).getActualTypeArguments()[0]);
			else
				t.setGenericType(genericReturnType.getClass());
			t.setParameterizedGenericTypeName(genericReturnType.toString());
		}

		return t;
	}
}