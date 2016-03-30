package co.xarx.trix.web.rest.api;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.TermView;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/terms")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface TermsApi {

	@GET
	@Path("/termTree")
	Response getTermTree(@QueryParam("taxonomyId") Integer taxonomyId,
						 @QueryParam("perspectiveId") Integer perspectiveId) throws IOException;

	@GET
	@Path("/{termId}/image")
	Response getTermImage(@PathParam("termId") Integer termId,
						  @QueryParam("perspectiveId") Integer perspectiveId) throws IOException;

	@GET
	@Path("/allTerms")
	@PreAuthorize("permitAll()")
	ContentResponse<List<TermView>> getAllTerms(@QueryParam("taxonomyId") Integer taxonomyId,
												@QueryParam("perspectiveId") Integer perspectiveId) throws IOException;

	@GET
	@Path("/search/findPostsByTagAndStationId")
	@PreAuthorize("permitAll()")
	ContentResponse<List<PostView>> findPostsByTagAndStationId(@QueryParam("tagName") String tagName,
															   @QueryParam("stationId") Integer stationId,
															   @QueryParam("page") int page,
															   @QueryParam("size") int size) throws ServletException, IOException;
}
