package co.xarx.trix.web.rest.api.v2;

import co.xarx.trix.api.v2.request.SectionsUpdateRequest;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/v2/stations/{stationId}/pages/{pageId}/sections")
@Produces(MediaType.APPLICATION_JSON)
public interface V2SectionsApi {

	@GET
	@Path("/")
	@PreAuthorize("hasPermission(#stationId, 'co.xarx.trix.domain.Station', 'read')")
	Response getSections(@PathParam("stationId") Integer stationId,
						 @PathParam("pageId") Integer pageId,
						 @QueryParam("size") @DefaultValue("10") Integer size,
						 @QueryParam("page") @DefaultValue("0") Integer page) throws IOException;

	@POST
	@Path("/")
	Response postSections(@PathParam("stationId") Integer stationId,
						  @PathParam("pageId") Integer pageId,
						  SectionsUpdateRequest sectionsUpdateRequest) throws IOException;
}
