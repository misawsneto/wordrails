package co.xarx.trix.web.rest.api.v1;

import co.xarx.trix.api.RowView;
import co.xarx.trix.api.TermPerspectiveView;
import co.xarx.trix.exception.ConflictException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/perspectives")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PerspectiveApi {

	@PUT
	@Path("/termPerspectiveDefinitions/{id}")
	Response putTermView(@PathParam("id") Integer id, TermPerspectiveView definition);

	@POST
	@Path("/termPerspectiveDefinitions")
	Response postTermView(TermPerspectiveView definition) throws ConflictException;

	@GET
	@Path("/termPerspectiveViews")
	TermPerspectiveView getTermPerspectiveView(@QueryParam("termPerspectiveId") Integer termPerspectiveId,
											   @QueryParam("termId") Integer termId,
											   @QueryParam("stationPerspectiveId") Integer stationPerspectiveId,
											   @QueryParam("page") int page,
											   @QueryParam("size") int size, @QueryParam("withBody") Boolean withBody);

	@GET
	@Path("/termPerspectiveDefinitions/{id}")
	TermPerspectiveView getTermPerspectiveDefinition(@PathParam("id") Integer id);

	@GET
	@Path("/rowViews")
	RowView getRowView(@QueryParam("stationPerspectiveId") Integer stationPerspectiveId,
					   @QueryParam("termPerspectiveId") Integer termPerspectiveId,
					   @QueryParam("childTermId") Integer childTermId,
					   @QueryParam("withBody") Boolean withBody,
					   @QueryParam("page") int page,
					   @QueryParam("size") int size);
}
