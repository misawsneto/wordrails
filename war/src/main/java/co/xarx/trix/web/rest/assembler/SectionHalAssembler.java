package co.xarx.trix.web.rest.assembler;

import co.xarx.trix.api.hal.QueryableSectionHal;
import co.xarx.trix.api.hal.SectionHal;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.QueryableSection;
import co.xarx.trix.domain.page.Section;
import co.xarx.trix.web.rest.resource.PagesResource;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.jaxrs.JaxRsLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class SectionHalAssembler implements ResourceAssembler<Section, SectionHal> {

	@Override
	public SectionHal toResource(Section entity) {
		SectionHal sectionHal = new SectionHal();

		if(entity instanceof QueryableSection) {
			sectionHal = new QueryableSectionHal();
			((QueryableSectionHal) sectionHal).setPageable(((QueryableSection) entity).isPageable());
			((QueryableSectionHal) sectionHal).setSize(((QueryableSection) entity).getSize());
		}

		return getSectionHal(entity, sectionHal);
	}

	public <S extends SectionHal> S getSectionHal(Section entity, S sectionHal) {
		sectionHal.setTitle(entity.getTitle());
		sectionHal.setType(entity.getType());
		sectionHal.setProperties(entity.getProperties());
		sectionHal.setBlocks(entity.getBlocks());
		sectionHal.setStyle(entity.getStyle().name().toLowerCase());

		Page page = entity.getPage();
		Link self = JaxRsLinkBuilder
				.linkTo(PagesResource.class)
				.slash(page.getStation().getId() + "/pages/" + page.getId() + "/sections/" + entity.getId())
				.withSelfRel();

		sectionHal.add(self);

		return sectionHal;
	}
}
