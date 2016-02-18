package co.xarx.trix.generator.domain;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.annotation.SdkInclude;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

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

	@Override
	public String getTypeName() {
		return field.getType().getName();
	}

	@Override
	public String getTypeSimpleName() {
		return field.getType().getSimpleName();
	}

	@Override
	public String getParametrizedGenericTypeName() {
		return field.getGenericType().toString();
	}

	@Override
	public String getNameUppercase() {
		return getNameUppercase(field.getName());
	}

	@Override
	public boolean isSdkInclude() {
		return field.isAnnotationPresent(SdkInclude.class);
	}

	@Override
	public boolean isSdkIncludeAsReference() {
		return field.isAnnotationPresent(NotNull.class) || field.isAnnotationPresent(SdkInclude.class);
	}

	@Override
	public boolean isSdkExclude() {
		return field.isAnnotationPresent(SdkExclude.class);
	}

	@Override
	public boolean isRelationship() {
		return field.isAnnotationPresent(OneToMany.class)
				|| field.isAnnotationPresent(ManyToMany.class)
				|| field.isAnnotationPresent(OneToOne.class)
				|| field.isAnnotationPresent(ManyToOne.class);
	}

	@Override
	public boolean isMappedBy() {
		return (
				field.isAnnotationPresent(OneToMany.class) && StringUtils.hasText(field.getAnnotation(OneToMany.class).mappedBy())
		) || (
				field.isAnnotationPresent(ManyToMany.class) && StringUtils.hasText(field.getAnnotation(ManyToMany.class).mappedBy())
		);
	}

	@Override
	public boolean isElementCollection() {
		return field.isAnnotationPresent(ElementCollection.class);
	}

	@Override
	public Class<?> getGenericType() {
		ParameterizedType parameterizedGenericType = (ParameterizedType) field.getGenericType();
		Type[] actualTypeArguments = parameterizedGenericType.getActualTypeArguments();
		Type actualTypeArgument = actualTypeArguments[actualTypeArguments.length - 1];
		return (Class<?>) actualTypeArgument;
	}
}
