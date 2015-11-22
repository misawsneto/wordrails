package co.xarx.trix.web.rest;

import co.xarx.trix.aspect.annotations.Profile;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.QPage;
import co.xarx.trix.domain.page.QueryableListSection;
import co.xarx.trix.domain.page.QueryableSection;
import co.xarx.trix.domain.query.ElasticSearchQuery;
import co.xarx.trix.domain.query.FixedQuery;
import co.xarx.trix.domain.query.PageableQuery;
import co.xarx.trix.persistence.FixedQueryRepository;
import co.xarx.trix.persistence.PageRepository;
import co.xarx.trix.persistence.PageableQueryRepository;
import co.xarx.trix.persistence.StationRepository;
import co.xarx.trix.services.PageService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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
	private PageService pageService;

	@Profile
	@GET
	@Path("/{stationId}/pages")
	public Iterable<Page> getPages(@PathParam("stationId") Integer stationId) throws IOException {
		QPage qPage = QPage.page;
		Iterable<Page> pages = pageRepository.findAll(qPage.station.id.eq(stationId));

		for (Page page : pages) {
			page.getSections().stream().filter(section -> section instanceof QueryableSection).forEach(section -> {
				pageService.fetchQueries((QueryableListSection) section, 0);
			});
		}

		return pages;
	}

	@POST
	@Path("/page")
	public Response postPage() {
		ElasticSearchQuery q1 = new ElasticSearchQuery();
		q1.setQueryString("{ \"bool\" : { \"must\" : [ { \"multi_match\" : { \"query\" : \"dilma\", \"fields\" : [ \"body^2.0\", \"title^5.0\", \"topper\", \"subheading\", \"authorName\", \"terms.name\" ], \"prefix_length\" : 1 } }, { \"match\" : { \"state\" : { \"query\" : \"PUBLISHED\", \"type\" : \"boolean\" } } }, { \"bool\" : { \"should\" : [ { \"match\" : { \"stationId\" : { \"query\" : \"11\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"14\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"55\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"75\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"76\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"77\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"137\", \"type\": \"boolean\" } } }]} } ]} }");
		q1.setObjectName("post");
		q1.setHighlightedField("body");

		PageableQuery pageableQuery = new PageableQuery();
		pageableQuery.setElasticSearchQuery(q1);

		pageableQueryRepository.save(pageableQuery);

		ElasticSearchQuery q2 = new ElasticSearchQuery();
		q2.setQueryString("{ \"bool\" : { \"must\" : [ { \"multi_match\" : { \"query\" : \"fhc\", \"fields\" : [ \"body^2.0\", \"title^5.0\", \"topper\", \"subheading\", \"authorName\", \"terms.name\" ], \"prefix_length\" : 1 } }, { \"match\" : { \"state\" : { \"query\" : \"PUBLISHED\", \"type\" : \"boolean\" } } }, { \"bool\" : { \"should\" : [ { \"match\" : { \"stationId\" : { \"query\" : \"11\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"14\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"55\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"75\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"76\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"77\", \"type\": \"boolean\" } } },{ \"match\" : { \"stationId\" : { \"query\" : \"137\", \"type\": \"boolean\" } } }]} } ]} }");
		q2.setObjectName("post");
		q2.setHighlightedField("body");
		FixedQuery fixedQuery1 = new FixedQuery();
		fixedQuery1.setElasticSearchQuery(q2);
		fixedQuery1.setIndexes(Sets.newHashSet(1, 2, 3));

		fixedQueryRepository.save(fixedQuery1);

		Station station = stationRepository.findOne(11);
		Page page = new Page();
		page.setTitle("Home");
		page.setStation(station);

		QueryableListSection section1 = new QueryableListSection();
		section1.setTitle("Section 1");
		section1.setPage(page);
		section1.setSize(10);
		section1.setPageable(true);
		section1.setPageableQuery(pageableQuery);
		section1.setFixedQueries(Lists.newArrayList(fixedQuery1));

		page.setSections(Lists.newArrayList(section1));

		pageRepository.save(page);

		return Response.ok().build();
	}
}
