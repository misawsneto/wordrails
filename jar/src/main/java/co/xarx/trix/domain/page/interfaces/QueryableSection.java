package co.xarx.trix.domain.page.interfaces;

import co.xarx.trix.domain.query.Query;

import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;

@MappedSuperclass
public interface QueryableSection<T extends Query> extends Section {

	@OneToOne
	T getQuery();

	void setQuery(T Query);
}
