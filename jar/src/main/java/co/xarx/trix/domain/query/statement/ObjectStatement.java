package co.xarx.trix.domain.query.statement;

import co.xarx.trix.domain.query.QueryBuilder;

public interface ObjectStatement<T> extends Statement {

	T build(QueryBuilder<T> builder);

	String getObjectType();
}
