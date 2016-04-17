package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.PageData;
import co.xarx.trix.api.v2.SectionData;
import co.xarx.trix.domain.page.*;
import co.xarx.trix.persistence.PageRepository;
import co.xarx.trix.persistence.SectionRepository;
import co.xarx.trix.services.QueryableSectionService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2PagesApi;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static co.xarx.trix.domain.page.QPage.page;

@Component
@NoArgsConstructor
public class V2PagesResource extends AbstractResource implements V2PagesApi {

	private PageRepository pageRepository;
	private SectionRepository sectionRepository;
	private QueryableSectionService queryableSectionService;
	private ModelMapper mapper;

	@Autowired
	public V2PagesResource(PageRepository pageRepository, SectionRepository sectionRepository,
					  QueryableSectionService queryableSectionService, ModelMapper mapper) {
		this.pageRepository = pageRepository;
		this.sectionRepository = sectionRepository;
		this.queryableSectionService = queryableSectionService;
		this.mapper = mapper;
	}

	@Override
	public Response getPages(Integer stationId) throws IOException {
		Iterable<Page> pages = pageRepository.findAll(page.station.id.eq(stationId));

		pages.forEach(this::populateQueryableSections);

		List<PageData> hals = new ArrayList<>();
		for (Page page : pages) {
			hals.add(mapper.map(page, PageData.class));
		}

		return Response.ok(hals).build();
	}

	@Override
	public Response getSections(Integer stationId, Integer pageId, Integer size, Integer page) throws IOException {
		Pageable pageable = new PageRequest(page, size);

		QPage p = QAbstractSection.abstractSection.page;
		org.springframework.data.domain.Page<AbstractSection> all = sectionRepository.findAll(p.id.eq(pageId),
				pageable);
		List<SectionData> sectionHals = new ArrayList<>();

		all.forEach(section -> sectionHals.add(mapper.map(section, SectionData.class)));

		return Response.ok(sectionHals).build();
	}

	private void populateQueryableSections(Page page) {
		page.getSectionList().stream()
				.filter(section -> section != null)
				.filter(section -> section instanceof QueryableSection)
				.forEach(section -> ((QueryableListSection) section).populate(queryableSectionService, 0));
	}
}
