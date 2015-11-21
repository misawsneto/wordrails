package co.xarx.trix.domain.query;

import java.util.List;

public interface ElasticSearchExecutor<T> {

	List execute(ElasticSearchQuery query, Integer size, Integer from);
}
