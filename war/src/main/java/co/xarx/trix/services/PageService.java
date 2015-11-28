package co.xarx.trix.services;

import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.QueryableSection;
import co.xarx.trix.domain.query.FixedQuery;
import co.xarx.trix.domain.query.PageableQuery;
import co.xarx.trix.domain.query.QueryExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class PageService {

	@Autowired
	private QueryExecutor queryExecutor;

	public Map<Integer, Block> fetchQueries(QueryableSection section, Integer from) {
		Map<Integer, Block> blocks = new TreeMap<>();

		PageableQuery pageableQuery = section.getPageableQuery();
		List<FixedQuery> fixedQueries = section.getFixedQueries();
		Map<Integer, Block> fixedBlocks = new TreeMap<>();
		fixedQueries.stream() //fetch all fixed blocks from all fixedqueries
				.forEach(fixedQuery -> fixedBlocks.putAll(fixedQuery.fetch(queryExecutor)));

		AtomicInteger pageableFrom = new AtomicInteger(from);
		fixedBlocks.keySet().stream() //get all fixedblocks that are going to show on this page
				.forEach(index -> {
					if (index < from) {
						pageableFrom.decrementAndGet();
					}
					if (index >= from && index < from + section.getSize()) {
						blocks.put(index, fixedBlocks.get(index));
						if(pageableQuery != null)
							pageableQuery.addIndexException(index);
					}

				});

		if(pageableQuery != null) {
			pageableQuery.setFrom(pageableFrom.get());

			//add boolean queries to the pageable stream avoid the items that were already got
			fixedBlocks.values().stream()
					.filter(block -> Objects.equals(block.getObjectType(), pageableQuery.getElasticSearchQuery().getObjectName()))
					.forEach(block -> pageableQuery.addIdException(block.getObject().getId()));

			Map<Integer, Block> pageBlocks = pageableQuery.fetch(queryExecutor);
			//add all elements that don't don't clash with some index
			pageBlocks.keySet().stream()
					.forEach(index -> blocks.putIfAbsent(index, pageBlocks.get(index)));
		}
		blocks.keySet().stream()
				.filter(index -> index >= section.getSize())
				.forEach(blocks::remove);

		return blocks;
	}
}
