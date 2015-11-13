package co.xarx.trix.domain.page;

import co.xarx.trix.domain.query.Query;

public interface QueryableSection<T extends Query> {

	T getQuery();
}
