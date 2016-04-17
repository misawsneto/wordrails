package co.xarx.trix.web.rest.api.v2;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v2/posts")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface V2PostApi {

	@GET
	@Path("/search")
	@Produces(MediaType.APPLICATION_JSON)
	Response searchPosts(@QueryParam("q") String query,
						 @QueryParam("authorId") Integer author,
						 @QueryParam("stationId") List<Integer> stations,
						 @QueryParam("state") String state,
						 @QueryParam("from") String from,
						 @QueryParam("until") String until,
						 @QueryParam("categoryId") List<Integer> categories,
						 @QueryParam("tag") List<String> tags,
						 @QueryParam("size") @DefaultValue("10") Integer size,
						 @QueryParam("page") @DefaultValue("0") Integer page,
						 @QueryParam("order") List<String> orders,
						 @QueryParam("embed") List<String> embeds);
}
