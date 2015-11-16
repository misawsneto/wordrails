package co.xarx.trix.web.rest;

import co.xarx.trix.domain.page.Page;
import co.xarx.trix.domain.page.QPage;
import co.xarx.trix.persistence.PageRepository;
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

	@GET
	@Path("/{stationId}/pages")
	public Response getPages(@PathParam("stationId") Integer stationId) throws IOException {
		QPage qPage = QPage.page;
		Iterable<Page> pages = pageRepository.findAll(qPage.station.id.eq(stationId));

		return Response.ok().build();
	}

	@POST
	@Path("/page")
	public Response postPage() {
		Page page = new Page();
		page.setTitle("Home");

		return Response.ok().build();
	}
}
