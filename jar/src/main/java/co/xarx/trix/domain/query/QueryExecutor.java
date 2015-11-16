package co.xarx.trix.domain.query;

import java.util.List;

public interface QueryExecutor<K, T extends Query<K>> {

	List<K> execute(T query);
}
