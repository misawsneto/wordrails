package co.xarx.trix.web.rest.api;

import co.xarx.trix.domain.Taxonomy;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/taxonomies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface TaxonomiesApi {

	@GET
	@Path("/search/findNetworkCategories")
	void findNetworkCategories() throws IOException;

	@GET
	@Path("/networks/{networkId}/taxonomiesToEdit")
	Response getTaxonomiesToEdit(@PathParam("networkId") Integer networkId) throws IOException;

	@GET
	@Path("/allCategories")
	List<Taxonomy> getCategories(@QueryParam("stationId") Integer stationId) throws IOException;
}
