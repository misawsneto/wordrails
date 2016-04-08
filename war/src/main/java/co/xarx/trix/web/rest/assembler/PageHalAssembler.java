package co.xarx.trix.web.rest.assembler;

import co.xarx.trix.api.hal.HalResources;
import co.xarx.trix.api.hal.PageHal;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.web.rest.resource.PagesResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.jaxrs.JaxRsLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class PageHalAssembler implements ResourceAssembler<Page, PageHal> {

	private SectionHalAssembler sectionHalAssembler;

	@Autowired
	public PageHalAssembler(SectionHalAssembler sectionHalAssembler) {
		this.sectionHalAssembler = sectionHalAssembler;
	}

	@Override
	public PageHal toResource(Page entity) {
		PageHal pageHal = new PageHal();
		pageHal.setTitle(entity.getTitle());

		Link self = JaxRsLinkBuilder
				.linkTo(PagesResource.class)
				.slash(entity.getStation().getId() + "/pages/" + entity.getId())
				.withSelfRel();
		Link sectionsSelf = JaxRsLinkBuilder
				.linkTo(PagesResource.class)
				.slash(entity.getStation().getId() + "/pages/" + entity.getId() + "/sections")
				.withSelfRel();

		pageHal.add(self);
		HalResources sections = HalResources.wrap(entity.getSectionList(), sectionHalAssembler);
		sections.add(sectionsSelf);
		pageHal.setSections(sections);

		return pageHal;
	}
}
