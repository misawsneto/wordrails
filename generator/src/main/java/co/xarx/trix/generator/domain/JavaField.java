package co.xarx.trix.generator.domain;

import co.xarx.trix.annotation.SdkInclude;
import org.springframework.util.StringUtils;

import javax.persistence.ElementCollection;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public class JavaField extends AbstractField {

	private Field field;

	public JavaField(Field field) {
		this.field = field;
	}

	@Override
	public boolean isId() {
		return field.isAnnotationPresent(Id.class);
	}

	@Override
	public boolean isList() {
		return field.isAnnotationPresent(OneToMany.class)
				|| field.isAnnotationPresent(ManyToMany.class)
				|| field.isAnnotationPresent(ElementCollection.class);
	}

	@Override
	public Class<?> getType() {
		return field.getType();
	}

	@Override
	public String getName() {
		return field.getName();
	}

	public String getParameterizedGenericTypeName() {
		return field.getGenericType().toString();
	}

	@Override
	public boolean isIncludeAsReference() {
		return field.isAnnotationPresent(SdkInclude.class) || field.isAnnotationPresent(ElementCollection.class);
	}

//	@Override
//	public boolean isRelationship() {
//		return field.isAnnotationPresent(OneToMany.class)
//				|| field.isAnnotationPresent(ManyToMany.class)
//				|| field.isAnnotationPresent(OneToOne.class)
//				|| field.isAnnotationPresent(ManyToOne.class);
//	}

	@Override
	public boolean isMappedBy() {
		return (
				field.isAnnotationPresent(OneToMany.class) && StringUtils.hasText(field.getAnnotation(OneToMany.class).mappedBy())
		) || (
				field.isAnnotationPresent(ManyToMany.class) && StringUtils.hasText(field.getAnnotation(ManyToMany.class).mappedBy())
		);
	}

	@Override
	public boolean isMap() {
		return getType().isAssignableFrom(Map.class);
	}

	@Override
	public Class<?> getGenericType() {
		ParameterizedType parameterizedGenericType;
		try {
			parameterizedGenericType = (ParameterizedType) field.getGenericType();
		} catch (Exception e) {
			return null;
		}
		Type[] actualTypeArguments = parameterizedGenericType.getActualTypeArguments();
		Type actualTypeArgument = actualTypeArguments[actualTypeArguments.length - 1];
		return (Class<?>) actualTypeArgument;
	}
}
