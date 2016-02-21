package co.xarx.trix.services;

import co.xarx.trix.domain.Identifiable;
import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.BlockImpl;
import co.xarx.trix.domain.query.FixedQuery;
import co.xarx.trix.domain.query.ObjectQuery;
import co.xarx.trix.domain.query.PageableQuery;
import co.xarx.trix.domain.query.QueryRunner;
import co.xarx.trix.domain.query.elasticsearch.ElasticSearchExecutor;
import co.xarx.trix.domain.query.elasticsearch.ElasticSearchQuery;
import co.xarx.trix.domain.query.statement.ObjectStatement;
import co.xarx.trix.factory.ElasticSearchExecutorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class QueryRunnerService implements QueryRunner {

	@Autowired
	private ElasticSearchQueryBuilder queryBuilder;
	@Autowired
	private ElasticSearchExecutorFactory elasticSearchExecutorFactory;

	private Map<Integer, Block> getBlocks(Iterator<Identifiable> itens, Iterator<Integer> indexes, String objectName) {
		Map<Integer, Block> blocks = new TreeMap<>();

		while (itens.hasNext() && indexes.hasNext()) {
			BlockImpl block = new BlockImpl(itens.next(), objectName);
			blocks.put(indexes.next(), block);
		}

		return blocks;
	}

	private List<Identifiable> getItens(ObjectQuery query, Integer size, Integer from) {
		String objectType = query.getObjectType();
		ObjectStatement<ElasticSearchQuery> objectStatement = query.getObjectStatement();
		ElasticSearchExecutor executor = elasticSearchExecutorFactory.getExecutor(objectType + "_executor");
		return executor.execute(objectStatement.build(queryBuilder), size, from);
	}

	@Override
	public Map<Integer, Block> execute(FixedQuery query) {
		List<Identifiable> itens = getItens(query, query.getIndexes().size(), 0);
		return getBlocks(itens.iterator(), query.getIndexes().iterator(), query.getObjectType());
	}

	@Override
	public Map<Integer, Block> execute(PageableQuery query) {
		List<Identifiable> itens = getItens(query, query.getSize(), query.getFrom());
		return getBlocks(itens.iterator(), query.getIndexes().iterator(), query.getObjectType());
	}
}
