package co.xarx.trix.domain.page.interfaces;

import co.xarx.trix.domain.query.Query;

public interface QueryableSection<T extends Query> extends Section {

	T getQuery();

	void setQuery(T Query);
}
