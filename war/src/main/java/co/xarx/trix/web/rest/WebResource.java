package co.xarx.trix.web.rest;

import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.page.BaseSection;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.PostListSection;
import co.xarx.trix.domain.page.QPage;
import co.xarx.trix.domain.page.interfaces.QueryableSection;
import co.xarx.trix.domain.page.interfaces.Section;
import co.xarx.trix.domain.query.ElasticSearchQuery;
import co.xarx.trix.domain.query.QueryExecutor;
import co.xarx.trix.persistence.BaseQueryRepository;
import co.xarx.trix.persistence.PageRepository;
import co.xarx.trix.persistence.StationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Path("/web")
@Component
public class WebResource {

	@Context
	private HttpServletRequest request;
	@Autowired
	private PageRepository pageRepository;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private BaseQueryRepository queryRepository;
	@Autowired
	private QueryExecutor queryExecutor;

	@GET
	@Path("/{stationId}/pages")
	public Response getPages(@PathParam("stationId") Integer stationId) throws IOException {
		QPage qPage = QPage.page;
		Iterable<Page> pages = pageRepository.findAll(qPage.station.id.eq(stationId));

		for (Page page : pages) {
			for(Section section : page.getSections()) {
				if(section instanceof QueryableSection) {
					((QueryableSection) section).getQuery().fetch(queryExecutor);
				}
			}
		}

		return Response.ok().build();
	}

	@POST
	@Path("/page")
	public Response postPage() {
		ElasticSearchQuery query = new ElasticSearchQuery();
		query.setQueryString("{\"bool\" : {\"must\" : [ {\"multi_match\" : {\"query\" : \"dilma\",\"fields\" : [ \"body^2.0\", \"title^5.0\", \"topper\", \"subheading\", \"authorName\", \"terms.name\" ],\"prefix_length\" : 1}}, {\"match\" : {\"state\" : {\"query\" : \"PUBLISHED\",\"type\" : \"boolean\"}}}, {\"bool\" : {\"should\" : [ {\"match\" : {\"stationId\" : {\"query\" : \"11\",\"type\" : \"boolean\"}}}, {\"match\" : {\"stationId\" : {\"query\" : \"14\",\"type\" : \"boolean\"}}}, {\"match\" : {\"stationId\" : {\"query\" : \"55\",\"type\" : \"boolean\"}}}, {\"match\" : {\"stationId\" : {\"query\" : \"75\",\"type\" : \"boolean\"}}}, {\"match\" : {\"stationId\" : {\"query\" : \"76\",\"type\" : \"boolean\"}}}, {\"match\" : {\"stationId\" : {\"query\" : \"77\",\"type\" : \"boolean\"}}}, {\"match\" : {\"stationId\" : {\"query\" : \"137\",\"type\" : \"boolean\"}}} ]}} ]}}");


		Station station = stationRepository.findOne(11);
		Page page = new Page();
		page.setTitle("Home");
		page.setStation(station);

		List<BaseSection> sections = new ArrayList<>();

		PostListSection section1 = new PostListSection();
		section1.setTitle("Section 1");
		section1.setPage(page);
		section1.setQuery(query);

		PostListSection section2 = new PostListSection();
		section2.setTitle("Section 2");
		section2.setPage(page);
		section2.setQuery(query);

		sections.add(section1);
		sections.add(section2);

		page.setSections(sections);

		pageRepository.save(page);

		return Response.ok().build();
	}
}
