package co.xarx.trix.web.rest.api.v2;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/v2/recommends")
@Consumes(MediaType.WILDCARD)
public interface V2RecommendApi {

	@GET
	@Path("/searchRecommends")
	@Produces(MediaType.APPLICATION_JSON)
	ContentResponse<List<PostView>> searchRecommends(@QueryParam("query") String q, @QueryParam("page") Integer page, @QueryParam("size") Integer size);

	@PUT
	@Path("/toggleRecommend")
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("isAuthenticated()")
	BooleanResponse toggleRecommend(@QueryParam("postId") Integer postId);
}
