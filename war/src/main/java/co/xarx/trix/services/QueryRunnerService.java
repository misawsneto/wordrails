package co.xarx.trix.services;

import co.xarx.trix.domain.Identifiable;
import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.BlockImpl;
import co.xarx.trix.domain.page.query.*;
import co.xarx.trix.domain.page.query.statement.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class QueryRunnerService implements QueryRunner {

	private ExecutorFactory executorFactory;

	protected QueryRunnerService() {
	}

	@Autowired
	public QueryRunnerService(ExecutorFactory executorFactory) {
		this.executorFactory = executorFactory;
	}

	private Map<Integer, Block> getBlocks(Iterator<Identifiable> itens, Iterator<Integer> indexes, Class objectType) {
		Map<Integer, Block> blocks = new TreeMap<>();

		while (itens.hasNext() && indexes.hasNext()) {
			BlockImpl block = new BlockImpl(itens.next(), objectType);
			blocks.put(indexes.next(), block);
		}

		return blocks;
	}

	private Iterable getItens(Query query, Integer size, Integer from) {
		String objectType = query.getType().getSimpleName().toLowerCase();
		Statement objectStatement = query.getObjectStatement();
		Executor executor = executorFactory.getExecutor(objectType + "_executor");
		return  executor.execute(objectStatement, size, from);
	}

	@Override
	public Map<Integer, Block> execute(FixedQuery query, Integer size, Integer from) {
		Iterable<Identifiable> itens = getItens(query, size, from);
		return getBlocks(itens.iterator(), query.getIndexes().iterator(), query.getType());
	}

	@Override
	public Map<Integer, Block> execute(PageableQuery query, Integer size, Integer from) {
		Iterable<Identifiable> itens = getItens(query, size, from);
		return getBlocks(itens.iterator(), query.getIndexes().iterator(), query.getType());
	}
}
