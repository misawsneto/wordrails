package co.xarx.trix.domain.query;

import java.util.List;

public interface Executor<T, K> {

	List<K> execute(T query, Integer size, Integer from);
}
