package co.xarx.trix.domain.query;

import co.xarx.trix.domain.query.statement.PostStatement;

public interface QueryBuilder<T> {

	T build(PostStatement query);
}
