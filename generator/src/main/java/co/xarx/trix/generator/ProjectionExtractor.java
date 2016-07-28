package co.xarx.trix.generator;

import co.xarx.trix.generator.exception.InvalidProjectionException;
import co.xarx.trix.generator.scope.EntityDescription;
import co.xarx.trix.generator.scope.FieldDescription;
import org.springframework.data.rest.core.config.Projection;

import javax.persistence.Entity;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ProjectionExtractor {

	private Set<Class<?>> projectionClasses;

	public ProjectionExtractor(Set<Class<?>> projectionClasses) {
		this.projectionClasses = projectionClasses;
	}

	public Map<Class<?>, EntityDescription> getProjections() throws InvalidProjectionException {
		Map<Class<?>, EntityDescription> projections = new HashMap<>();

		for (Class<?> projectionClass : projectionClasses) {
			Projection projectionAnnotation = projectionClass.getAnnotation(Projection.class);

			Class<?>[] entityClasses = projectionAnnotation.types();

			EntityDescription projection = getProjection(projectionClass);

			for (Class<?> entityClass : entityClasses) {
				projections.put(entityClass, projection);
			}
		}

		return projections;
	}

	public EntityDescription getProjection(Class<?> projectionClass) {
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
				type = returnType.getSimpleName() + (!GeneratorUtil.isSimpleType(returnType) ? "Dto" : "");
			}
			if (collectionType != null) {
				type = collectionType.getName() + "<" + returnType.getSimpleName() + (!GeneratorUtil.isSimpleType(returnType) ? "Dto" : "") + ">";
			}
			FieldDescription fieldDescription = new FieldDescription();
			fieldDescription.type = type;
			fieldDescription.name = method.getName();
			if (fieldDescription.name.startsWith("get")) {
				fieldDescription.name = GeneratorUtil.getLowercase(fieldDescription.name.substring("get".length()));
			}
			projection.fields.add(fieldDescription);
		}
		return projection;
	}

	public EntityDescription getProjectionEntityDescription(Class<?> projectionClass) {
		EntityDescription projection = new EntityDescription();
		projection.fullName = projectionClass.getName();
		projection.name = projectionClass.getSimpleName();
		projection.nameLowercase = GeneratorUtil.getLowercase(projection.name);
		projection.plural = GeneratorUtil.getPlural(projection.name);
		projection.pluralLowercase = GeneratorUtil.getLowercase(projection.plural);
		return projection;
	}
}