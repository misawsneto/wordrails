package co.xarx.trix.domain.query;

import co.xarx.trix.domain.page.Block;

import java.util.Map;

public interface QueryExecutor {

	Map<Integer, Block> execute(FixedQuery query);

	Map<Integer, Block> execute(PageableQuery query);
}
