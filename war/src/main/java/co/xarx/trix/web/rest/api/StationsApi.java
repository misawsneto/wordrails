package co.xarx.trix.web.rest.api;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.domain.page.Page;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/stations")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface StationsApi {

	@GET
	@Path("/")
	void getStations() throws ServletException, IOException;

	@POST
	@Path("/")
	void postStation() throws ServletException, IOException;

	@PUT
	@Path("/{id}")
	void putStation() throws ServletException, IOException;

	@DELETE
	@Path("/{id}")
	void deleteStation() throws ServletException, IOException;

	@GET
	@Path("/{id}")
	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Station', 'read')")
	void getStation(@PathParam("id") @P("id") int stationId) throws ServletException, IOException;

	@GET
	@Path("/{id}/stationPerspectives")
	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Station', 'read')")
	void getStationPerspectives(@PathParam("id") @P("id") int stationId) throws ServletException, IOException;

	@GET
	@Path("/{stationId}/pages")
	@Produces(MediaType.APPLICATION_JSON)
	List<Page> getPages(@PathParam("stationId") Integer stationId) throws IOException;

	@PUT
	@Path("/{stationId}/setDefaultPerspective")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	Response setDefaultPerspective(@PathParam("stationId") Integer stationId, @FormParam("perspectiveId") Integer perspectiveId);

	@PUT
	@Path("/{stationId}/setMainStation")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	Response setMainStation(@PathParam("stationId") Integer stationId, @FormParam("value") boolean value);

	@GET
	@Path("/stats/roles/count")
	ContentResponse<Integer> countRolesByStationIds(@QueryParam("stationIds") List<Integer> stationIds, @QueryParam("q") String q);
}
