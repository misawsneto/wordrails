package co.xarx.trix.web.rest.api;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/recommends")
@Consumes(MediaType.WILDCARD)
public interface RecommendsApi {

	@GET
	@Path("/searchRecommends")
	@Produces(MediaType.APPLICATION_JSON)
	ContentResponse<List<PostView>> searchRecommends(@QueryParam("query") String q,
													 @QueryParam("page") Integer page,
													 @QueryParam("size") Integer size);

	@PUT
	@Path("/toggleRecommend")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("isAuthenticated()")
	BooleanResponse toggleRecommend(@FormParam("postId") Integer postId);
}
