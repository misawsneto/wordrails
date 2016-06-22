package co.xarx.trix.web.rest.api.v1;

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
	@Path("/")
	void getTerms() throws IOException;

	@GET
	@Path("/{id}/posts")
	void getTermsPosts() throws IOException;

	@POST
	@Path("/")
	void postTerm() throws IOException;

	@PUT
	@Path("/{id}")
	void putTerm() throws IOException;

	@DELETE
	@Path("/{id}")
	void deleteTerm() throws IOException;

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
	@Path("/search/findPostsByTerm")
	ContentResponse<List<PostView>> findPostsByTerm(@QueryParam("termId") Integer termId,
													@QueryParam("page") int page,
													@QueryParam("size") int size,
													@QueryParam("sort") String sort) throws ServletException,
			IOException;

	@GET
	@Path("/search/findPostsByCategory")
	@PreAuthorize("permitAll()")
	public ContentResponse<List<PostView>> findPostsByCategory(@QueryParam("categoryName") String categoryName,
															   @QueryParam("stationId") Integer stationId, @QueryParam("page") int page, @QueryParam("size") int size);
}
