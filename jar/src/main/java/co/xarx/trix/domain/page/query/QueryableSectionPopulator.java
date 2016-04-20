package co.xarx.trix.domain.page.query;

import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.QueryableSection;

import java.util.Map;

public interface QueryableSectionPopulator {

	Map<Integer, Block> fetchQueries(QueryableSection section, Integer from);
}
