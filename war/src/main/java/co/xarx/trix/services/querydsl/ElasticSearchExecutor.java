package co.xarx.trix.services.querydsl;

import co.xarx.trix.domain.query.Query;
import co.xarx.trix.domain.query.QueryExecutor;

import java.util.List;

public class ElasticSearchExecutor implements QueryExecutor {


	@Override
	public <T> List<T> execute(Query<T> query) {
		return null;
	}
}
