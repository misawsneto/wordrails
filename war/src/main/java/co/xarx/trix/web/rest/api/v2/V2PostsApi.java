package co.xarx.trix.web.rest.api.v2;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.v2.PostData;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v2/posts")
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface V2PostsApi {

	@GET
	@Path("/search")
	Response searchPosts(@QueryParam("q") String query,
						 @QueryParam("authorId") List<Integer> authors,
						 @QueryParam("stationId") List<Integer> stations,
						 @QueryParam("state") String state,
						 @QueryParam("from") String from,
						 @QueryParam("until") String until,
						 @QueryParam("categoryId") List<Integer> categories,
						 @QueryParam("tag") List<String> tags,
						 @QueryParam("page") @DefaultValue("0") Integer page,
						 @QueryParam("size") @DefaultValue("10") Integer size,
						 @QueryParam("order") List<String> orders,
						 @QueryParam("embed") List<String> embeds);

	@GET
	@Path("/findPostsByIds")
	ContentResponse<List<PostView>> findPostsByIds (@QueryParam("ids") List<Integer> ids);

	@GET
	@Path("/getPostVersions")
	ContentResponse<List<PostData>> getPostVersions(@QueryParam("postId") Integer postId) throws NoSuchFieldException, IllegalAccessException;
}
