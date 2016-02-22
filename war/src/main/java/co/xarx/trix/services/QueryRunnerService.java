package co.xarx.trix.services;

import co.xarx.trix.domain.Identifiable;
import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.BlockImpl;
import co.xarx.trix.domain.query.*;
import co.xarx.trix.domain.query.statement.Statement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class QueryRunnerService implements QueryRunner {

	@Autowired
	private CommandBuilder commandBuilder;
	@Autowired
	private ExecutorFactory executorFactory;

	private Map<Integer, Block> getBlocks(Iterator<Identifiable> itens, Iterator<Integer> indexes, Class objectType) {
		Map<Integer, Block> blocks = new TreeMap<>();

		while (itens.hasNext() && indexes.hasNext()) {
			BlockImpl block = new BlockImpl(itens.next(), objectType);
			blocks.put(indexes.next(), block);
		}

		return blocks;
	}

	private List<Identifiable> getItens(Query query, Integer size, Integer from) {
		String objectType = query.getType().getSimpleName();
		Statement objectStatement = query.getObjectStatement();
		Executor executor = executorFactory.getExecutor(objectType + "_executor");
		return  executor.execute(objectStatement.build(commandBuilder), size, from);
	}

	@Override
	public Map<Integer, Block> execute(FixedQuery query) {
		List<Identifiable> itens = getItens(query, query.getIndexes().size(), 0);
		return getBlocks(itens.iterator(), query.getIndexes().iterator(), query.getType());
	}

	@Override
	public Map<Integer, Block> execute(PageableQuery query) {
		List<Identifiable> itens = getItens(query, query.getSize(), query.getFrom());
		return getBlocks(itens.iterator(), query.getIndexes().iterator(), query.getType());
	}
}
