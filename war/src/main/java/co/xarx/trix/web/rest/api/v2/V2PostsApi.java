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

	@POST
	@Path("/{postId}/publish")
	Response publish(@PathParam("postId") Integer postId);

	@POST
	@Path("/{postId}/unpublish")
	Response unpublish(@PathParam("postId") Integer postId);

	@PUT
	@Path("/{postId}/unpublish/{date}")
	Response scheduleUnpublishing(@PathParam("postId") Integer postId, @PathParam("date") String date);


	@GET
	@Path("/findPostsByIds")
	ContentResponse<List<PostView>> findPostsByIds(@QueryParam("ids") List<Integer> ids);

	@GET
	@Path("/getPostVersions")
	ContentResponse<List<PostData>> getPostVersions(@QueryParam("postId") Integer postId) throws NoSuchFieldException, IllegalAccessException;

	class BulkUpdateState{
		public List<Integer> ids;
		public String state;
	}

	@PUT
	@Path("/bulkUpdateStates")
	Response bulkUpdateStates(BulkUpdateState bulkUpdateState) throws NoSuchFieldException,
			IllegalAccessException;

	@PUT
	@Path("/{postId}/seen")
	Response setPostSeen(@PathParam("postId") Integer postId,
						 @QueryParam("timeReading") Integer timeReading,
						 @QueryParam("date") Long timestamp);
}
