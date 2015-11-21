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

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Service
public class PageService {

	@Autowired
	private QueryExecutor queryExecutor;

	public Map<Integer, Block> fetchQueries(QueryableSection section, Integer from) {
		Map<Integer, Block> blocks = new TreeMap<>();

		List<FixedQuery> fixedQueries = section.getFixedQueries();
		Map<Integer, Block> fixedBlocks = new TreeMap<>();
		fixedQueries.stream()
				.forEach(fixedQuery -> fixedBlocks.putAll(fixedQuery.fetch(queryExecutor)));

		fixedBlocks.keySet().stream()
				.filter(index -> index >= from && index < from + section.getSize())
				.forEach(index -> blocks.put(index, fixedBlocks.get(index)));

		PageableQuery pageableQuery = section.getPageableQuery();
		pageableQuery.setFrom(from);

		//add boolean queries to the pageable stream avoid the items that were already got
		fixedBlocks.values().stream()
				.filter(block -> Objects.equals(block.getObjectName(), pageableQuery.getElasticSearchQuery().getObjectName()))
				.forEach(block ->
						pageableQuery.getElasticSearchQuery().getBoolQueryBuilder().mustNot(matchQuery("id", block.getId()))
				);

		Map<Integer, Block> pageBlocks = pageableQuery.fetch(queryExecutor);
		//add all elements that don't don't clash with some index
		pageBlocks.keySet().stream()
				.forEach(index -> blocks.putIfAbsent(index, pageBlocks.get(index)));
		blocks.keySet().stream()
				.filter(index -> index >= section.getSize())
				.forEach(blocks::remove);

		return blocks;
	}
}
