package co.xarx.trix.domain.page;

import co.xarx.trix.domain.query.FixedQuery;
import co.xarx.trix.domain.query.PageableQuery;

import java.util.List;

public interface QueryableSection extends Section {

	PageableQuery getPageableQuery();

	void setPageableQuery(PageableQuery pageableQuery);

	List<FixedQuery> getFixedQueries();

	void setFixedQueries(List<FixedQuery> fixedQueries);

	boolean isPageable();

	void setPageable(boolean pageable);

	Integer getSize();

	void setSize(Integer size);
}
