package co.xarx.trix.services.page;

import co.xarx.trix.api.v2.request.SaveSectionsRequest;
import co.xarx.trix.domain.page.ContainerSection;
import co.xarx.trix.domain.page.QueryableSection;
import co.xarx.trix.domain.page.Section;
import co.xarx.trix.domain.page.query.PageableQuery;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class SectionFactoryTest {

	SectionFactory sectionFactory = new SectionFactory(new ModelMapper());
	SaveSectionsRequest sectionsRequest = new SaveSectionsRequest();

	@Before
	public void setUp() throws Exception {

	}

	@Test
	public void getSection() throws Exception {
		val postStatement = sectionsRequest.new PostStatement();
		postStatement.setAuthors(Arrays.asList(1,2,3));
		postStatement.setQuery("dilma");
		val pageableQuery = sectionsRequest.new PageableQuery();
		pageableQuery.setStatement(postStatement);
		val s1 = sectionsRequest.new QueryableSection();
		s1.setPageableQuery(pageableQuery);

		val containerSection = sectionsRequest.new ContainerSection();
		containerSection.setStyle(Section.Style.CARROUSEL.toString());

		containerSection.add(s1);

		ContainerSection section = (ContainerSection) sectionFactory.getSection(containerSection);

		assertThat(section.getStyle(), is(Section.Style.CARROUSEL));
		assertEquals(section.getChildren().size(), 1);
		QueryableSection queryableSection = (QueryableSection) section.getChildren().values().iterator().next();
		assertEquals(section.getType(), Section.Type.CONTAINER);
		PageableQuery pq = queryableSection.getPageableQuery();
		assertNotNull(pq);
		assertTrue(pq.getObjectStatement() instanceof PostStatement);
		assertEquals(((PostStatement) pq.getObjectStatement()).getQuery(), "dilma");
	}

}