package co.xarx.trix.services;

import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Identifiable;
import co.xarx.trix.domain.page.Block;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.page.QueryableSection;
import co.xarx.trix.domain.page.query.*;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;

public class QueryableSectionServiceTest {

	private static final Integer PAGEABLE_ID_BASE = 30;
	private static final Integer FIXED_ID_BASE = 80;

	QueryableSectionService service;
	QueryableSection section;
	List<Integer> fixedIndexes = Lists.newArrayList(2, 8, 13, 1, 15);
	Integer iFixed;

	private class PostStatementMock extends PostStatement {
		boolean isFixed;

		public PostStatementMock(boolean isFixed) {
			this.isFixed = isFixed;
		}

		@Override
		public Command build(CommandBuilder builder) {
			return new CommandMock(isFixed);
		}
	}

	private class CommandMock implements Command {
		boolean isFixed;

		public CommandMock(boolean isFixed) {
			this.isFixed = isFixed;
		}
	}

	private class ExecutorFactoryMock implements ExecutorFactory {
		@Override
		public Executor getExecutor(String alias) {
			return new ExecutorMock();
		}
	}

	private class ExecutorMock implements Executor<Identifiable, CommandMock> {

		@Override
		public List<Identifiable> execute(CommandMock command, Integer size, Integer from) {
			List<Identifiable> result = new ArrayList<>();
			for (int i = from; i < from + size; i++) {
				PostView pv = new PostView();
				if (command.isFixed) {
					pv.setPostId(fixedIndexes.get(iFixed++) + FIXED_ID_BASE);
				} else {
					pv.setPostId(i + PAGEABLE_ID_BASE);
				}
				result.add(pv);
			}
			return result;
		}
	}

	@Before
	public void setUp() throws Exception {
		PostStatement fixedPS = new PostStatementMock(true);

		List<FixedQuery> fixedQueries = new ArrayList<>();
		FixedQuery f1 = new FixedQuery(fixedPS, Lists.newArrayList(2, 8));
		FixedQuery f2 = new FixedQuery(fixedPS, Lists.newArrayList(13));
		FixedQuery f3 = new FixedQuery(fixedPS, Lists.newArrayList(1, 15));
		fixedQueries.add(f1);
		fixedQueries.add(f2);
		fixedQueries.add(f3);

		PostStatement pageablePS = new PostStatementMock(false);

		section = new QueryableListSection(10, fixedQueries);
		section.setPageableQuery(new PageableQuery(pageablePS));

		QueryRunner qr = new QueryRunnerService(null, new ExecutorFactoryMock());
		service = new QueryableSectionService(qr);
	}

	@Test
	public void testPageOne() throws Exception {
		iFixed = 0;
		Map<Integer, Block> blocks = service.fetchQueries(section, 0);

		List<Serializable> expectedIdList = Lists.newArrayList(30, 81, 82, 31, 32, 33, 34, 35, 88, 36);
		List<Serializable> idList = blocks.values().stream().map(block -> block.getObject().getId()).collect(Collectors.toList());

		assertEquals(expectedIdList, idList);
	}

	@Test
	public void testPageTwo() throws Exception {
		iFixed = 0;
		Map<Integer, Block> blocks = service.fetchQueries(section, section.getSize());

		List<Serializable> expectedIdList = Lists.newArrayList(37, 38, 39, 93, 40, 95, 41, 42, 43, 44);
		List<Serializable> idList = blocks.values().stream().map(block -> block.getObject().getId()).collect(Collectors.toList());

		assertEquals(expectedIdList, idList);
	}

	@Test
	public void testPageThree() throws Exception {
		iFixed = 0;
		Map<Integer, Block> blocks = service.fetchQueries(section, section.getSize() * 2);

		List<Serializable> expectedIdList = Lists.newArrayList(45, 46, 47, 48, 49, 50, 51, 52, 53, 54);
		List<Serializable> idList = blocks.values().stream().map(block -> block.getObject().getId()).collect(Collectors.toList());

		assertEquals(expectedIdList, idList);
	}
}