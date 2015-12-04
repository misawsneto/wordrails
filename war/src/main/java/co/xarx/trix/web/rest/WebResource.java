package co.xarx.trix.web.rest;

import co.xarx.trix.aspect.annotations.Profile;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.page.*;
import co.xarx.trix.domain.query.FixedQuery;
import co.xarx.trix.domain.query.PageableQuery;
import co.xarx.trix.domain.query.PostQuery;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.PageService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Path("/web")
@Component
public class WebResource {

	@Context
	private HttpServletRequest request;
	@Autowired
	private PageRepository pageRepository;
	@Autowired
	private FixedQueryRepository fixedQueryRepository;
	@Autowired
	private PageableQueryRepository pageableQueryRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private BaseSectionRepository sectionRepository;
	@Autowired
	private BaseObjectQueryRepository objectQueryRepository;
	@Autowired
	private PageService pageService;

	@Profile
	@GET
	@Path("/{stationId}/pages")
	@Produces(MediaType.APPLICATION_JSON)
	public List<Page> getPages(@PathParam("stationId") Integer stationId) throws IOException {
		QPage qPage = QPage.page;
		Iterable<Page> pages = pageRepository.findAll(qPage.station.id.eq(stationId));

		for (Page page : pages) {
			page.getSections().values().stream()
					.filter(section -> section != null)
					.filter(section -> section instanceof QueryableSection)
					.forEach(section -> {
						Map<Integer, Block> blocks = pageService.fetchQueries((QueryableListSection) section, 0);
						if (section instanceof ListSection) {
							((ListSection) section).setBlocks(blocks);
						}
					});
		}


		return Lists.newArrayList(pages);
	}

	@POST
	@Path("/page")
	public Response postPage() {
		PostQuery postQuery1 = new PostQuery();
		postQuery1.setStationIds(Lists.newArrayList(11));
		postQuery1.setRichText("dilma");
		objectQueryRepository.save(postQuery1);

		PageableQuery pageableQuery = new PageableQuery();
		pageableQuery.setObjectQuery(postQuery1);
		pageableQueryRepository.save(pageableQuery);

		PostQuery postQuery2 = new PostQuery();
		postQuery2.setStationIds(Lists.newArrayList(11));
		postQuery2.setRichText("fhc");
		objectQueryRepository.save(postQuery2);

		FixedQuery fixedQuery1 = new FixedQuery();
		fixedQuery1.setObjectQuery(postQuery2);
		fixedQuery1.setIndexes(Sets.newHashSet(0, 2, 3));

		fixedQueryRepository.save(fixedQuery1);

		FixedQuery fixedQuery2 = new FixedQuery();
		fixedQuery2.setObjectQuery(postQuery2);
		fixedQuery2.setIndexes(Sets.newHashSet(0, 1, 2, 3, 4));

		fixedQueryRepository.save(fixedQuery2);

		Station station = stationRepository.findOne(11);
		Page page = new Page();
		page.setTitle("Home");
		page.setStation(station);

		QueryableListSection section1 = new QueryableListSection();
		section1.setTitle("Section 1");
		section1.setSize(10);
		section1.setPageable(true);
		section1.setPageableQuery(pageableQuery);
		section1.setFixedQueries(Lists.newArrayList(fixedQuery1));
		sectionRepository.save(section1);

		QueryableListSection section2 = new QueryableListSection();
		section2.setTitle("Section 2");
		section2.setSize(5);
		section2.setPageable(false);
		section2.setFixedQueries(Lists.newArrayList(fixedQuery2));
		sectionRepository.save(section2);

		page.setSections(new TreeMap<Integer, BaseSection>() {{put(0, section1); put(1, section2);}});

		pageRepository.save(page);

		return Response.ok().build();
	}
}
