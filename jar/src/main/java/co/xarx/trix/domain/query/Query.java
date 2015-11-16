package co.xarx.trix.domain.query;

import org.springframework.core.GenericTypeResolver;

import java.util.List;

public interface Query<T> {

	String getQuery();

	QueryExecutor getExecutor();

	void setExecutor(QueryExecutor executor);

	List<T> getResults();

	default String getClassName() {
		return GenericTypeResolver.resolveTypeArgument(getClass(), Query.class).getSimpleName();
	}
}
