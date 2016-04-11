package co.xarx.trix.generator;

import java.util.HashSet;
import java.util.Set;

public class GeneratorUtil {

	public static String getPlural(String noun) {
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

	public static String getLowercase(String name) {
		return name.substring(0, 1).toLowerCase() + name.substring(1);
	}

	static boolean isSimpleType(Class<?> clazz){
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
