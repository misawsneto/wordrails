package co.xarx.trix.web.rest.api;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/bookmarks")
@Consumes(MediaType.WILDCARD)
public interface BookmarksApi {

	@GET
	@Path("/searchBookmarks")
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasRole('ROLE_USER')")
	ContentResponse<List<PostView>> searchBookmarks(@QueryParam("query") String q,
													@QueryParam("page") Integer page,
													@QueryParam("size") Integer size);

	@PUT
	@Path("/toggleBookmark")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasRole('ROLE_USER')")
	BooleanResponse toggleBookmark(@FormParam("postId") Integer postId);
}
