package co.xarx.trix.services;

import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.QueryableSection;
import co.xarx.trix.domain.page.query.FixedQuery;
import co.xarx.trix.domain.page.query.PageableQuery;
import co.xarx.trix.domain.page.query.QueryRunner;
import co.xarx.trix.domain.page.query.SectionPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SectionService implements SectionPopulator {

	private QueryRunner queryRunner;

	@Autowired
	public SectionService(QueryRunner queryRunner) {
		this.queryRunner = queryRunner;
	}

	@Override
	public Map<Integer, Block> fetchQueries(QueryableSection section, Integer from) {
		Map<Integer, Block> fixedBlocks = getFixedBlocks(section.getFixedQueries());
		Map<Integer, Block> blocks = getBlocksInCurrentPage(section, from, fixedBlocks);

		PageableQuery pageableQuery = getPageableQuery(section, from);
		if(pageableQuery != null) {
			Integer fixedBlocksBeforeFrom = getFixedBlocksBeforeFrom(from, fixedBlocks);
			blocks.keySet().stream().forEach(pageableQuery::addIndexException);
			pageableQuery.setStartShift(fixedBlocksBeforeFrom);
			//add boolean queries to the pageable stream avoid the items that were already got
			excludeFixedElementsFromPageableQuery(pageableQuery, fixedBlocks);

			Map<Integer, Block> pageBlocks = pageableQuery.fetch(queryRunner);
			addPageBlocksToBlocks(blocks, pageBlocks); //add all elements that don't clash with some index
		}

		excludeExtraBlocks(section, from, blocks);

		return blocks;
	}

	private void excludeExtraBlocks(QueryableSection section, Integer from, Map<Integer, Block> blocks) {
		Map<Integer, Block> tmpBlocks = new TreeMap<>(blocks);
		tmpBlocks.keySet().stream()
				.filter(index -> index >= section.getSize() + from)
				.forEach(blocks::remove);
	}

	private void addPageBlocksToBlocks(Map<Integer, Block> blocks, Map<Integer, Block> pageBlocks) {
		pageBlocks.keySet().stream()
				.forEach(index -> blocks.putIfAbsent(index, pageBlocks.get(index)));
	}

	private Map<Integer, Block> getBlocksInCurrentPage(QueryableSection section, Integer from, Map<Integer, Block> fixedBlocks) {
		Map<Integer, Block> blocks = new TreeMap<>();
		fixedBlocks.keySet().stream() //get all fixedblocks that are going to show on this page
				.filter(index -> index >= from && index < from + section.getSize()) //belongs to page
				.forEach(index -> blocks.put(index, fixedBlocks.get(index)));
		return blocks;
	}

	private Integer getFixedBlocksBeforeFrom(Integer from, Map<Integer, Block> fixedBlocks) {
		AtomicInteger fixedBlocksBeforeFrom = new AtomicInteger();
		fixedBlocks.keySet().stream() //get all fixedblocks that are going to show on this page
				.filter(index -> index < from)
				.forEach(index -> fixedBlocksBeforeFrom.incrementAndGet());
		return fixedBlocksBeforeFrom.get();
	}

	private void excludeFixedElementsFromPageableQuery(PageableQuery pageableQuery, Map<Integer, Block> fixedBlocks) {
		fixedBlocks.values().stream()
				.filter(block -> Objects.equals(block.getObjectType(), pageableQuery.getType()))
				.forEach(block -> pageableQuery.addIdExclusion(block.getObject().getId()));
	}

	private Map<Integer, Block> getFixedBlocks(List<FixedQuery> fixedQueries) {
		Map<Integer, Block> fixedBlocks = new TreeMap<>();
		fixedQueries.stream() //fetch all fixed blocks from all fixedqueries
				.forEach(fixedQuery -> fixedBlocks.putAll(fixedQuery.fetch(queryRunner)));
		return fixedBlocks;
	}

	private PageableQuery getPageableQuery(QueryableSection section, Integer from) {
		PageableQuery pageableQuery = section.getPageableQuery();
		pageableQuery.setFrom(from);
		pageableQuery.setSize(section.getSize());
		return pageableQuery;
	}
}
