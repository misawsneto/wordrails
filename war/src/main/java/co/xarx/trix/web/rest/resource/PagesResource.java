package co.xarx.trix.web.rest.resource;

import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.page.QueryableSection;
import co.xarx.trix.persistence.PageRepository;
import co.xarx.trix.services.QueryableSectionService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.PagesApi;
import co.xarx.trix.api.hal.HalResources;
import co.xarx.trix.api.hal.PageHal;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.jaxrs.JaxRsLinkBuilder;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static co.xarx.trix.domain.page.QPage.page;

@Component
@NoArgsConstructor
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PagesResource extends AbstractResource implements PagesApi {

	@NonNull
	private PageRepository pageRepository;
	@NonNull
	private QueryableSectionService queryableSectionService;
	@NonNull
	private ResourceAssembler<Page, PageHal> assembler;

	@Override
	public Response getPages(Integer stationId) throws IOException {
		Iterable<Page> pages = pageRepository.findAll(page.station.id.eq(stationId));

		pages.forEach(this::populateQueryableSections);

		List<PageHal> hals = new ArrayList<>();
		for (Page page : pages) {
			hals.add(assembler.toResource(page));
		}

		HalResources<PageHal> response = new HalResources<>(hals);
		Link self = JaxRsLinkBuilder
				.linkTo(PagesResource.class)
				.slash(stationId + "/pages")
				.withSelfRel();
		response.add(self);

		return Response.ok(response).build();
	}

	private void populateQueryableSections(Page page) {
		page.getSectionList().stream()
				.filter(section -> section != null)
				.filter(section -> section instanceof QueryableSection)
				.forEach(section -> ((QueryableListSection) section).populate(queryableSectionService, 0));
	}
}
