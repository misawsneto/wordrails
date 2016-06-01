package co.xarx.trix.web.rest.api.v2;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/v2/bookmarks")
@Consumes(MediaType.WILDCARD)
public interface V2BookmarksApi {

	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("isAuthenticated()")
	ContentResponse<List<PostView>> searchBookmarks(@QueryParam("query") String q, @QueryParam("page") Integer page, @QueryParam("size") Integer size);

	@PUT
	@Path("/toggle/{postId}")
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("isAuthenticated()")
	BooleanResponse toggleBookmark(@PathParam("postId") Integer postId);
}
