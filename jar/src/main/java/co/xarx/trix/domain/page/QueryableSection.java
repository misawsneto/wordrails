package co.xarx.trix.domain.page;

import co.xarx.trix.domain.page.query.FixedQuery;
import co.xarx.trix.domain.page.query.PageableQuery;
import co.xarx.trix.domain.page.query.SectionPopulator;

import java.util.List;

public interface QueryableSection extends Section {

	PageableQuery getPageableQuery();

	List<FixedQuery> getFixedQueries();

	void setFixedQueries(List<FixedQuery> fixedQueries);

	boolean isPageable();

	Integer getSize();

	void populate(SectionPopulator populator, Integer from);
}
