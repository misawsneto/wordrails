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
	private QueryBuilderExecutor queryBuilderExecutor;
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

	private List<Identifiable> getItens(Query query, Integer size, Integer from) {
		List<Identifiable> itens = new ArrayList<>();
		String objectType = query.getObjectType();
		ObjectQuery objectQuery = query.getObjectQuery();
		if(objectQuery instanceof ElasticSearchObjectQuery) {
			ElasticSearchExecutor executor = elasticSearchExecutorFactory.getExecutor(objectType + "_executor");
			itens = executor.execute((ElasticSearchQuery) objectQuery.build(queryBuilderExecutor), size, from);
		}

		return itens;
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
