package co.xarx.trix.services.page;

import co.xarx.trix.api.v2.request.SaveSectionsRequest;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.AbstractSection;
import co.xarx.trix.domain.page.ContainerSection;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.page.Section;
import co.xarx.trix.domain.page.query.FixedQuery;
import co.xarx.trix.domain.page.query.PageableQuery;
import co.xarx.trix.domain.page.query.statement.AbstractStatement;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import lombok.val;
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

	public AbstractSection getSection(SaveSectionsRequest.Section sectionRequest) {
		AbstractSection section = null;

		if(sectionRequest.getType().equals(Section.Type.CONTAINER.toString())) {
			val sr = (SaveSectionsRequest.ContainerSection) sectionRequest;
			ContainerSection containerSection = new ContainerSection();
			for (SaveSectionsRequest.Section ch : sr.getChildren().values()) {
				containerSection.addChild(getSection(ch));
			}

			section = containerSection;
		} else if (sectionRequest.getType().equals(Section.Type.QUERYABLE.toString())) {
			val sr = (SaveSectionsRequest.QueryableSection) sectionRequest;
			val pageableQuery = getPageableQuery(sr.getPageableQuery());
			val fixedQueries = getFixedQueries(sr.getFixedQueries());

			section = new QueryableListSection(sr.getSize(), pageableQuery, fixedQueries);
		}

		setDefaultAttributes(sectionRequest, section);

		return section;
	}

	private void setDefaultAttributes(SaveSectionsRequest.Section sr, AbstractSection section) {
		if (sr.getStyle() != null) {
			section.setStyle(Section.Style.valueOf(sr.getStyle().toUpperCase()));
		}
		section.setTitle(sr.getTitle());
		section.setBottomMargin(sr.getBottomMargin());
		section.setTopMargin(sr.getTopMargin());
		section.setLeftMargin(sr.getLeftMargin());
		section.setRightMargin(sr.getRightMargin());
	}

	private List<FixedQuery> getFixedQueries(List<SaveSectionsRequest.FixedQuery> queryRequests) {
		List<FixedQuery> result = new ArrayList<>();
		for (SaveSectionsRequest.FixedQuery queryRequest : queryRequests) {
			AbstractStatement statement = getStatement(queryRequest.getStatement());
			result.add(new FixedQuery(statement, queryRequest.getIndexes()));
		}

		return result;
	}

	private PageableQuery getPageableQuery(SaveSectionsRequest.PageableQuery queryRequest) {
		AbstractStatement statement = getStatement(queryRequest.getStatement());
		return new PageableQuery(statement);
	}

	private AbstractStatement getStatement(SaveSectionsRequest.Statement statementRequest) {
		if(statementRequest.getType().equals(Post.class)) {
			return mapper.map(statementRequest, PostStatement.class);
		}

		return null;
	}
}
