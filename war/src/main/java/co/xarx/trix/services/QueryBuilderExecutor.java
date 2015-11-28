package co.xarx.trix.services;

import co.xarx.trix.domain.query.ElasticSearchQuery;
import co.xarx.trix.domain.query.PostQuery;
import co.xarx.trix.domain.query.QueryBuilder;
import org.springframework.stereotype.Component;

@Component
public class QueryBuilderExecutor implements QueryBuilder {

	@Override
	public ElasticSearchQuery build(PostQuery query) {
		return null;
	}
}
