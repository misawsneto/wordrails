package co.xarx.trix.domain.query;

public interface QueryBuilder<T> {

	T build(PostQuery query);
}
