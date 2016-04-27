package co.xarx.trix.web.rest.api.v2;

import co.xarx.trix.api.v2.save.SavePageRequest;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/v2/stations")
@Produces(MediaType.APPLICATION_JSON)
public interface V2PagesApi {

	@GET
	@Path("/{stationId}/pages")
	@PreAuthorize("hasPermission(#sid, 'co.xarx.trix.domain.Station', 'read')")
	Response getPages(@PathParam("stationId") @P("sid") Integer stationId) throws IOException;

	@POST
	@Path("/{stationId}/pages")
	@Consumes(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasPermission(#sid, 'co.xarx.trix.domain.Station', 'administration')")
	Response postPage(@PathParam("stationId") @P("sid") Integer stationId,
					 SavePageRequest pageRequest) throws IOException;

	@PUT
	@Path("/{stationId}/pages/{pageId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasPermission(#sid, 'co.xarx.trix.domain.Station', 'administration')")
	Response putPage(@PathParam("stationId") @P("sid") Integer stationId,
					 @PathParam("pageId") Integer pageId,
					 SavePageRequest pageRequest) throws IOException;

	@GET
	@Path("/{stationId}/pages/{pageId}/sections")
	@PreAuthorize("hasPermission(#stationId, 'co.xarx.trix.domain.Station', 'read')")
	Response getSections(@PathParam("stationId") Integer stationId,
						 @PathParam("pageId") Integer pageId,
						 @QueryParam("size") @DefaultValue("10") Integer size,
						 @QueryParam("page") @DefaultValue("0") Integer page) throws IOException;
}
