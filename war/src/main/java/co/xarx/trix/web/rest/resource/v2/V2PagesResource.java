package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.PageData;
import co.xarx.trix.api.v2.request.PageUpdateRequest;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.page.QueryableSection;
import co.xarx.trix.persistence.PageRepository;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.services.SectionPopulatorService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2PagesApi;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static co.xarx.trix.domain.page.QPage.page;

@Component
@NoArgsConstructor
public class V2PagesResource extends AbstractResource implements V2PagesApi {

	@Autowired
	private PageRepository pageRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private SectionPopulatorService queryableSectionService;
	@Autowired
	private ModelMapper mapper;

	@AllArgsConstructor
	class IdReturn {
		public Integer id;
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

	private void populateQueryableSections(Page page) {
		page.getSectionList().stream()
				.filter(section -> section != null)
				.filter(section -> section instanceof QueryableSection)
				.forEach(section -> ((QueryableListSection) section).populate(queryableSectionService, 0));
	}

	@Override
	public Response postPage(Integer stationId, PageUpdateRequest pageRequest) throws IOException {
		Page page = mapper.map(pageRequest, Page.class);
		Station station = stationRepository.findOne(stationId);

		page.setStation(station);

		pageRepository.save(page);
		return Response
				.status(Response.Status.CREATED)
				.entity(new IdReturn(page.getId()))
				.build();
	}

	@Override
	public Response putPage(Integer stationId, Integer pageId, PageUpdateRequest pageRequest) throws IOException {
		Page page = pageRepository.findOne(pageId);

		if(page == null) {
			return unprocessableEntity("Page of id " + pageId + " could not be found");
		} else if(!Objects.equals(page.getStation().getId(), stationId)) {
			return unprocessableEntity("Page " + pageId + " does not belong to station " + stationId);
		}

		mapper.map(pageRequest, page);
		pageRepository.save(page);

		return Response
				.ok()
				.entity(new IdReturn(page.getId()))
				.build();
	}
}
