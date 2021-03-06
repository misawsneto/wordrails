package co.xarx.trix.domain.page.query;

import co.xarx.trix.domain.page.Block;

import java.util.Map;

public interface QueryRunner {

	Map<Integer, Block> execute(FixedQuery query, Integer size, Integer from);

	Map<Integer, Block> execute(PageableQuery query, Integer size, Integer from);
}
