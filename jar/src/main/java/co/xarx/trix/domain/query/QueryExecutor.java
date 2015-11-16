package co.xarx.trix.domain.query;

import co.xarx.trix.domain.page.interfaces.Block;

import java.util.List;

public interface QueryExecutor {

	List<Block> execute(ElasticSearchQuery query);
}
