package co.xarx.trix.web.rest;

import co.xarx.trix.aspect.annotations.Profile;
import co.xarx.trix.domain.page.*;
import co.xarx.trix.persistence.PageRepository;
import co.xarx.trix.services.PageService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Path("/web")
@Component
public class WebResource {

	@Autowired
	private PageRepository pageRepository;
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
}
