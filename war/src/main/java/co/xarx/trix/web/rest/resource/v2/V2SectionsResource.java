package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.SectionData;
import co.xarx.trix.api.v2.request.SaveSectionsRequest;
import co.xarx.trix.domain.page.AbstractSection;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.QAbstractSection;
import co.xarx.trix.domain.page.QPage;
import co.xarx.trix.persistence.SectionRepository;
import co.xarx.trix.services.page.PageService;
import co.xarx.trix.services.page.SectionService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2SectionsApi;
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

@Component
@NoArgsConstructor
public class V2SectionsResource extends AbstractResource implements V2SectionsApi {

	private PageService pageService;
	private SectionService sectionService;
	private SectionRepository sectionRepository;
	private ModelMapper mapper;

	@Autowired
	public V2SectionsResource(PageService pageService, SectionService sectionService,
							  SectionRepository sectionRepository, ModelMapper mapper) {
		this.pageService = pageService;
		this.sectionService = sectionService;
		this.sectionRepository = sectionRepository;
		this.mapper = mapper;
	}

	@Override
	public Response getSections(Integer stationId, Integer pageId, Integer size, Integer page) throws IOException {
		Pageable pageable = new PageRequest(page, size);

		if (pageService.findPage(stationId, pageId) == null) {
			return unprocessableEntity("Page of id " + pageId + " in station " + stationId + " could not be found");
		}

		QPage p = QAbstractSection.abstractSection.page;
		org.springframework.data.domain.Page<AbstractSection> all = sectionRepository.findAll(p.id.eq(pageId),
				pageable);
		List<SectionData> sectionData = new ArrayList<>();

		all.forEach(section -> sectionData.add(mapper.map(section, SectionData.class)));

		return Response.ok(sectionData).build();
	}

	@Override
	public Response postSections(Integer stationId, Integer pageId, SaveSectionsRequest saveSectionsRequest) throws IOException {
		Page page = pageService.findPage(stationId, pageId);
		if (page == null) {
			return unprocessableEntity("Page of id " + pageId + " in station " + stationId + " could not be found");
		}

		List<Integer> ids = sectionService.saveSections(page, saveSectionsRequest);

		return Response.ok(ids).build();
	}
}
