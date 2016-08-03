package co.xarx.trix.web.rest.api.v2;

import co.xarx.trix.api.v2.CommentData;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/v2/comments")
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface V2CommentsApi {

	@GET
	@Path("/search")
	Response searchComments(@QueryParam("q") String query,
							@QueryParam("authorId") List<Integer> authors,
							@QueryParam("postId") List<Integer> posts,
							@QueryParam("stationId") List<Integer> stations,
							@QueryParam("from") String from,
							@QueryParam("until") String until,
							@QueryParam("page") @DefaultValue("0") Integer page,
							@QueryParam("size") @DefaultValue("10") Integer size,
							@QueryParam("order") List<String> orders,
							@QueryParam("embed") List<String> embeds);

	@PUT
	@Path("/")
	void deleteComment(List<CommentData> comments) throws IOException;
}
