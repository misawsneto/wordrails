package co.xarx.trix.services.querydsl;

import co.xarx.trix.domain.page.PostBlock;
import co.xarx.trix.domain.query.ElasticSearchQuery;
import co.xarx.trix.domain.query.QueryExecutor;

import java.util.List;

public class ElasticSearchExecutor implements QueryExecutor<PostBlock, ElasticSearchQuery<PostBlock>> {


	@Override
	public List<PostBlock> execute(ElasticSearchQuery<PostBlock> query) {
		return null;
	}
}
