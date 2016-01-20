package co.xarx.trix.domain.query;

import java.util.List;

public interface ElasticSearchExecutor<K> extends Executor<ElasticSearchQuery, K> {

	List<K> execute(ElasticSearchQuery query, Integer size, Integer from);
}
