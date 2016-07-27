package co.xarx.trix.generator.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public abstract class AbstractField {

	private static final String JAVA_LANG = "java.lang.";

	protected String getType(Class<?> type) {
		String simpleName;
		String name = type.getName();
		if (name.startsWith(JAVA_LANG)) {
			simpleName = name.substring(JAVA_LANG.length());
		} else {
			simpleName = name;
		}
		return simpleName;
	}

	protected String getNameUppercase(String name) {
		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public boolean isSimpleType(Class<?> clazz) {
		Set<Class<?>> ret = new HashSet<>();
		ret.add(Boolean.class);
		ret.add(Character.class);
		ret.add(Byte.class);
		ret.add(Short.class);
		ret.add(Integer.class);
		ret.add(Long.class);
		ret.add(Float.class);
		ret.add(Double.class);
		ret.add(Serializable.class);
		ret.add(Date.class);
		ret.add(String.class);

		return ret.contains(clazz) || clazz.isPrimitive();
	}

	public String getNameUppercase() {
		return getNameUppercase(getName());
	}

	public String getTypeName() {
		return getType().getName();
	}

	public String getTypeSimpleName() {
		return getType().getSimpleName();
	}

	public abstract boolean isId();

	public abstract boolean isList();

	public abstract boolean isIncludeAsReference();

	public boolean isRelationship() {
		if(getGenericType() != null && !getGenericType().equals(Class.class)) {
			return !isSimpleType(getGenericType());
		}

		return !isSimpleType(getType());
	}

	public abstract boolean isMappedBy();

	public abstract boolean isMap();

	public abstract Class<?> getType();

	public abstract Class<?> getGenericType();

	public abstract String getName();

	public abstract String getParameterizedGenericTypeName();
}
