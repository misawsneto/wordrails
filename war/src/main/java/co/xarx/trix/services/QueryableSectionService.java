package co.xarx.trix.services;

import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.QueryableSection;
import co.xarx.trix.domain.page.query.FixedQuery;
import co.xarx.trix.domain.page.query.PageableQuery;
import co.xarx.trix.domain.page.query.QueryRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class QueryableSectionService {

	private QueryRunner queryRunner;

	@Autowired
	public QueryableSectionService(QueryRunner queryRunner) {
		this.queryRunner = queryRunner;
	}

	public Map<Integer, Block> fetchQueries(QueryableSection section, Integer from) {
		Map<Integer, Block> blocks = new TreeMap<>();

		PageableQuery pageableQuery = section.getPageableQuery();
		pageableQuery.setFrom(from);
		pageableQuery.setSize(section.getSize());

		List<FixedQuery> fixedQueries = section.getFixedQueries();
		Map<Integer, Block> fixedBlocks = new TreeMap<>();
		fixedQueries.stream() //fetch all fixed blocks from all fixedqueries
				.forEach(fixedQuery -> fixedBlocks.putAll(fixedQuery.fetch(queryRunner)));

		AtomicInteger startShift = new AtomicInteger();
		fixedBlocks.keySet().stream() //get all fixedblocks that are going to show on this page
				.forEach(index -> {
					if (index < from) {
						startShift.incrementAndGet();
					}
					if (index >= from && index < from + section.getSize()) {
						blocks.put(index, fixedBlocks.get(index));
						if(pageableQuery != null)
							pageableQuery.addIndexException(index);
					}
				});

		if(pageableQuery != null) {
			pageableQuery.setStartShift(startShift.get());
			//add boolean queries to the pageable stream avoid the items that were already got
			fixedBlocks.values().stream()
					.filter(block -> Objects.equals(block.getObjectType(), pageableQuery.getType()))
					.forEach(block -> pageableQuery.addIdExclusion(block.getObject().getId()));

			Map<Integer, Block> pageBlocks = pageableQuery.fetch(queryRunner);
			//add all elements that don't clash with some index
			pageBlocks.keySet().stream().forEach(index -> blocks.putIfAbsent(index, pageBlocks.get(index)));
		}
		Map<Integer, Block> tmpBlocks = new TreeMap<>(blocks);
		tmpBlocks.keySet().stream()
				.filter(index -> index >= section.getSize() + from)
				.forEach(blocks::remove);

		return blocks;
	}
}
