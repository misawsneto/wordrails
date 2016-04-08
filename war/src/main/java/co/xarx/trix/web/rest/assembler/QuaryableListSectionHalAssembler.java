package co.xarx.trix.web.rest.assembler;

import co.xarx.trix.api.hal.QueryableSectionHal;
import co.xarx.trix.domain.page.QueryableSection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class QuaryableListSectionHalAssembler implements ResourceAssembler<QueryableSection, QueryableSectionHal> {

	private SectionHalAssembler sectionHalAssembler;

	@Autowired
	public QuaryableListSectionHalAssembler(SectionHalAssembler sectionHalAssembler) {
		this.sectionHalAssembler = sectionHalAssembler;
	}

	@Override
	public QueryableSectionHal toResource(QueryableSection entity) {
		QueryableSectionHal sectionHal = sectionHalAssembler.getSectionHal(entity, new QueryableSectionHal());
		sectionHal.setSize(entity.getSize());
		sectionHal.setPageable(entity.isPageable());
		return sectionHal;
	}
}
