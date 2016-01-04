package co.xarx.trix.domain.query;

public interface ObjectQuery<T> {

	T build(QueryBuilder builder);

	String getObjectType();
}
