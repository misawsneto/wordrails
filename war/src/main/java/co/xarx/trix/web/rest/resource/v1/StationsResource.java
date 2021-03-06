package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.annotation.TimeIt;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.page.*;
import co.xarx.trix.eventhandler.StationEventHandler;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.SectionPopulatorService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.StationsApi;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class StationsResource extends AbstractResource implements StationsApi {

	@Autowired
	private AuthService authProvider;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private StationPerspectiveRepository stationPerspectiveRepository;
	@Autowired
	private QueryPersistence queryPersistence;

	@Autowired
	private PageRepository pageRepository;

	@Autowired
	private SectionPopulatorService queryableSectionService;

	@Override
	public void getStations() throws ServletException, IOException {
		forward();
	}

	@Override
	public void postStation() throws ServletException, IOException {
		forward();
	}

	@Override
	public void putStation() throws ServletException, IOException {
		forward();
	}

	@Override
	public void deleteStation() throws ServletException, IOException {
		forward();
	}

	@Override
	public void getStation(int stationId) throws ServletException, IOException {
		forward();
	}

	@Override
	public void getStationPerspectives(int stationId) throws ServletException, IOException {
		forward();
	}

	@Override
	@TimeIt
	public List<Page> getPages(Integer stationId) throws IOException {
		QPage qPage = QPage.page;
		Iterable<Page> pages = pageRepository.findAll(qPage.station.id.eq(stationId));

		for (Page page : pages) {
			page.getSections().values().stream().filter(section -> section != null).filter(section -> section instanceof QueryableSection).forEach(section -> {
				Map<Integer, Block> blocks = queryableSectionService.fetchQueries((QueryableListSection) section, 0);
//				if (section instanceof ListSection) {
					((QueryableListSection) section).blocks.putAll(blocks);
//				}
			});
		}


		return Lists.newArrayList(pages);
	}

	@Override
	public Response setMainStation(Integer stationId, boolean value) {
		Person person = authProvider.getLoggedPerson();

		if (person.networkAdmin) {
			for (Station station : stationRepository.findAll()) {
				station.main = station.id.equals(stationId) && value;

				stationRepository.save(station);
			}
			return Response.status(Status.OK).build();
		} else return Response.status(Status.UNAUTHORIZED).build();
	}

	private
	@Autowired
	StationEventHandler stationEventHandler;

	@Override
	public Response setDefaultPerspective(Integer stationId, Integer perspectiveId) {
		Station station = stationRepository.findOne(stationId);

		if (stationPerspectiveRepository.findOne(perspectiveId).stationId.equals(station.id)) {
			queryPersistence.updateDefaultPerspective(station.id, perspectiveId);
			return Response.status(Status.OK).build();
		} else return Response.status(Status.UNAUTHORIZED).build();
	}

	@Override
	public Response forceDelete(Integer stationId) {
		Station station = stationRepository.findOne(stationId);

		stationEventHandler.handleBeforeDelete(station);
		stationRepository.delete(station.id);
		return Response.status(Status.OK).build();
	}
}
