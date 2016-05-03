package co.xarx.trix.util;

import co.xarx.trix.api.v2.*;
import co.xarx.trix.domain.page.AbstractSection;
import co.xarx.trix.domain.page.ContainerSection;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.page.Section;
import co.xarx.trix.domain.page.query.FixedQuery;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.web.rest.mapper.ContainerSectionMapper;
import co.xarx.trix.web.rest.mapper.FixedQueryMapper;
import co.xarx.trix.web.rest.mapper.PostStatementMapper;
import co.xarx.trix.web.rest.mapper.QueryableSectionMapper;
import lombok.val;
import org.javers.common.collections.Sets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class MapStructMapperTest {

	@Configuration
	@ComponentScan("co.xarx.trix.web.rest.mapper")
	static class Config {
	}

	@Autowired
	FixedQueryMapper fixedQueryMapper;
	@Autowired
	PostStatementMapper postStatementMapper;
	@Autowired
	ContainerSectionMapper containerSectionMapper;
	@Autowired
	QueryableSectionMapper queryableSectionMapper;

	@Test
	public void testPageableStatementDataToEntity() {
		val dto = getPostStatement();

		PostStatement st = postStatementMapper.asEntity(dto);

		assertEquals(st.getQuery(), "dilma");
		assertEquals(st.getAuthors(), Arrays.asList(1, 2, 3));
		assertEquals(st.getTags(), Arrays.asList("uno", "dos", "tres"));
	}

	private PostStatementData getPostStatement() {
		val postStatement = new PostStatementData();
		postStatement.setExceptionIds(Sets.asSet(1, 2, 3));
		postStatement.setAuthors(Arrays.asList(1, 2, 3));
		postStatement.setQuery("dilma");
		postStatement.setTags(Arrays.asList("uno", "dos", "tres"));
		return postStatement;
	}

	@Test
	public void testFixedQueryDataToEntity() {
		val dto = new FixedQueryData();
		dto.setIndexes(Arrays.asList(1,2,3));
		dto.setStatement(getPostStatement());

		FixedQuery entity = fixedQueryMapper.asEntity(dto);

		assertEquals(entity.getIndexes(), Arrays.asList(1, 2, 3));
		assertEquals(entity.getObjectStatement().getExceptionIds(), dto.getStatement().getExceptionIds());
	}

	@Test
	public void testContainerSectionDataToEntity() {
		val dto = new ContainerSectionData();
		setDefaultAttributes(dto);

		ContainerSection entity = containerSectionMapper.asEntity(dto);

		assertSection(entity);
	}

	@Test
	public void testQueryableSectionDataToEntity() {
		val dto = new QueryableSectionData();
		setDefaultAttributes(dto);
		dto.setPageable(true);
		dto.setSize(10);

		QueryableListSection entity = queryableSectionMapper.asEntity(dto);

		assertSection(entity);
		assertThat(entity.getSize(), is(10));
		assertEquals(entity.isPageable(), true);
	}

	private void assertSection(AbstractSection entity) {
		assertEquals(entity.getTitle(), "Dummy title");
		assertThat(entity.getBottomMargin(), is(1));
		assertThat(entity.getTopMargin(), is(2));
		assertThat(entity.getLeftMargin(), is(3));
		assertThat(entity.getRightMargin(), is(4));
		assertThat(entity.getBottomPadding(), is(10));
		assertThat(entity.getTopPadding(), is(20));
		assertThat(entity.getLeftPadding(), is(30));
		assertThat(entity.getRightPadding(), is(40));
		assertThat(entity.getOrderPosition(), is(100));
		assertThat(entity.getPctSize(), is(75));
		assertThat(entity.getOrientation(), is(Section.Orientation.HORIZONTAL));
	}

	private void setDefaultAttributes(SectionData sr) {
		if (sr.getStyle() != null) {
			sr.setStyle(Section.Style.CARROUSEL.toString());
		}
		sr.setTitle("Dummy title");
		sr.setBottomMargin(1);
		sr.setTopMargin(2);
		sr.setLeftMargin(3);
		sr.setRightMargin(4);
		sr.setBottomPadding(10);
		sr.setTopPadding(20);
		sr.setLeftPadding(30);
		sr.setRightPadding(40);
		sr.setOrderPosition(100);
		sr.setOrientation(Section.Orientation.HORIZONTAL.toString());
		sr.setPctSize(75);
	}

}
