package co.xarx.trix.util;

import org.aspectj.lang.JoinPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class AspectUtil {

	public static List<Object> collectArguments(JoinPoint jp) {
		List<Object> result = new ArrayList<>();

		for (Object arg : jp.getArgs()) {
			if (arg instanceof Collection) {
				result.addAll((Collection) arg);
			} else {
				result.add(arg);
			}
		}
		return result;
	}

	public static <T> T getArgument(JoinPoint jp, Integer index, Class<T> type) {
		List<Object> args = collectArguments(jp);

		T arg;
		try {
			arg = (T) args.get(index);
		} catch (ClassCastException e) {
			throw new IllegalArgumentException("index " + index + " does not contain a " + type.getSimpleName());
		}

		return arg;
	}
}
