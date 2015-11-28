package co.xarx.trix.services;

import co.xarx.trix.domain.Identifiable;
import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.BlockImpl;
import co.xarx.trix.domain.query.*;
import co.xarx.trix.factory.ElasticSearchExecutorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QueryExecutorService implements QueryExecutor {

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

	@Override
	public Map<Integer, Block> execute(FixedQuery query) {
		ElasticSearchQuery esQuery = query.getElasticSearchQuery();
		ElasticSearchExecutor executor = elasticSearchExecutorFactory.getElasticSearchExecutor(esQuery.getObjectName() + "_executor");

		Set<Integer> indexes = query.getIndexes();
		List<Identifiable> itens = executor.execute(esQuery, indexes.size(), 0);
		return getBlocks(itens.iterator(), indexes.iterator(), esQuery.getObjectName());
	}

	@Override
	public Map<Integer, Block> execute(PageableQuery query) {
		ElasticSearchQuery esQuery = query.getElasticSearchQuery();
		ElasticSearchExecutor executor = elasticSearchExecutorFactory.getElasticSearchExecutor(esQuery.getObjectName() + "_executor");

		Set<Integer> indexes = query.getIndexes();
		List<Identifiable> itens = executor.execute(esQuery, query.getSize(), query.getFrom());
		return getBlocks(itens.iterator(), indexes.iterator(), esQuery.getObjectName());
	}
}
