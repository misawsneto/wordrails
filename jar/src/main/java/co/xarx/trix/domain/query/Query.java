package co.xarx.trix.domain.query;

import co.xarx.trix.domain.page.interfaces.Block;
import org.springframework.core.GenericTypeResolver;

import java.util.List;

public interface Query {

	String getQuery();

	Integer getSize();

	Integer getPage();

	void setSize(Integer size);

	void setPage(Integer page);

	List<Block> getResults(QueryExecutor executor);

	default String getClassName() {
		return GenericTypeResolver.resolveTypeArgument(getClass(), Query.class).getSimpleName();
	}
}
