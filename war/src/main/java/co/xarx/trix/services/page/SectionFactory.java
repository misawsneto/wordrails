package co.xarx.trix.services.page;

import co.xarx.trix.api.v2.*;
import co.xarx.trix.domain.page.AbstractSection;
import co.xarx.trix.domain.page.ContainerSection;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.page.Section;
import co.xarx.trix.domain.page.query.FixedQuery;
import co.xarx.trix.domain.page.query.PageableQuery;
import co.xarx.trix.domain.page.query.statement.AbstractStatement;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SectionFactory {

	private ModelMapper mapper;

	@Autowired
	public SectionFactory(ModelMapper mapper) {
		this.mapper = mapper;
	}

	public AbstractSection getSection(SectionData sectionRequest) {
		AbstractSection section = null;

		if(sectionRequest.getType().equals(Section.Type.CONTAINER.toString())) {
			ContainerSectionData sr = (ContainerSectionData) sectionRequest;
			ContainerSection containerSection = new ContainerSection();
			for (SectionData ch : sr.getChildren().values()) {
				containerSection.addChild(getSection(ch));
			}

			section = containerSection;
		} else if (sectionRequest.getType().equals(Section.Type.QUERYABLE.toString())) {
			QueryableSectionData sr = (QueryableSectionData) sectionRequest;
			PageableQuery pageableQuery = getPageableQuery(sr.getPageableQuery());
			List<FixedQuery> fixedQueries = getFixedQueries(sr.getFixedQueries());

			section = new QueryableListSection(sr.getSize(), pageableQuery, fixedQueries);
		}

		setDefaultAttributes(sectionRequest, section);

		return section;
	}

	private void setDefaultAttributes(SectionData sr, AbstractSection section) {
		if (sr.getStyle() != null) {
			section.setStyle(Section.Style.valueOf(sr.getStyle().toUpperCase()));
		}
		section.setTitle(sr.getTitle());
		section.setBottomMargin(sr.getBottomMargin());
		section.setTopMargin(sr.getTopMargin());
		section.setLeftMargin(sr.getLeftMargin());
		section.setRightMargin(sr.getRightMargin());
	}

	private List<FixedQuery> getFixedQueries(List<FixedQueryData> queryRequests) {
		List<FixedQuery> result = new ArrayList<>();
		for (FixedQueryData queryRequest : queryRequests) {
			AbstractStatement statement = getStatement(queryRequest.getStatement());
			result.add(new FixedQuery(statement, queryRequest.getIndexes()));
		}

		return result;
	}

	private PageableQuery getPageableQuery(PageableQueryData queryRequest) {
		AbstractStatement statement = getStatement(queryRequest.getStatement());
		return new PageableQuery(statement);
	}

	private AbstractStatement getStatement(AbstractStatementData statementRequest) {
		if(statementRequest instanceof PostStatementData) {
			return mapper.map(statementRequest, PostStatement.class);
		}

		return null;
	}
}
