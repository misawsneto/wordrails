package co.xarx.trix.web.rest.api.v1;

import co.xarx.trix.api.*;
import co.xarx.trix.api.v2.PostData;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.servlet.ServletException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public interface PostApi {

	@GET
	@Path("/")
	void getPosts() throws ServletException, IOException;

	@GET
	@Path("/search/findBySlug")
	void findBySlug() throws ServletException, IOException;

	@GET
	@Path("/{postId}/comments")
	@PreAuthorize("hasPermission(#postId, 'co.xarx.trix.domain.Post', 'read')")
	void getComments(@PathParam("postId") Integer postId) throws ServletException, IOException;

	@GET
	@Path("/{id:\\d+}")
	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Post', 'read') or hasRole('ADMIN')")
	void getPost(@PathParam("id") @P("id") int postId) throws ServletException, IOException;

	@PUT
	@Path("/{id:\\d+}")
	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Post', 'write') or hasRole('ADMIN')")
	void putPost(@PathParam("id") Integer id) throws ServletException, IOException;

	@POST
	@Path("/")
	void postPost() throws ServletException, IOException;

	@DELETE
	@Path("/{id:\\d+}")
	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Post', 'delete') or hasRole('ADMIN')")
	void deletePost(@PathParam("id") Integer id) throws ServletException, IOException;


	@POST
	@Path("/{postId}/comments")
	@PreAuthorize("hasPermission(#p, 'co.xarx.trix.domain.Post', 'read') or hasRole('ADMIN')")
	void postComment(@PathParam("postId") @P("p") Integer postId) throws ServletException,
			IOException;


	@GET
	@Path("/{postId}/terms")
	@PreAuthorize("hasPermission(#p, 'co.xarx.trix.domain.Post', 'read') or hasRole('ADMIN')")
	void getTerms(@PathParam("postId") @P("p") Integer postId) throws ServletException,
			IOException;


	@PUT
	@Path("/{postId}/comments/{commentId}")
	@PreAuthorize("hasPermission(#p, 'co.xarx.trix.domain.Post', 'read')")
	void putComment(@PathParam("postId") @P("p") Integer postId, @PathParam("commentId") Integer commentId) throws ServletException,
			IOException;

	@PUT
	@Path("/{postId}/updatePostTags")
	ContentResponse<PostView> updatePostTerms(@PathParam("postId") Integer postId, List<TermDto> terms) throws
			ServletException, IOException;

	@GET
	@Path("/getPostViewBySlug")
	@PostAuthorize("hasPermission(#returnObject.postId, 'co.xarx.trix.domain.Post', 'read') or returnObject==null")
	PostView getPostViewBySlug(@QueryParam("slug") String slug, @QueryParam("withBody") Boolean withBody)
			throws ServletException, IOException;

	@GET
	@Path("/{postId}/getPostViewById")
	@PostAuthorize("hasPermission(#postId, 'co.xarx.trix.domain.Post', 'read')")
	PostView getPostViewById(@PathParam("postId") Integer postId, @QueryParam("withBody") Boolean withBody)
			throws ServletException, IOException;

	@GET
	@Path("/{stationId}/findPostsByStationIdAndAuthorIdAndState")
	ContentResponse<List<PostView>> findPostsByStationIdAndAuthorIdAndState(@PathParam("stationId") Integer stationId,
																			@QueryParam("authorId") Integer authorId,
																			@QueryParam("state") String state,
																			@QueryParam("page") int page,
																			@QueryParam("size") int size)
			throws ServletException, IOException;

	@Deprecated
	@GET
	@Path("/search/networkPosts")
	ContentResponse<SearchView> searchPosts(@QueryParam("query") String q,
											@QueryParam("stationIds") String stationIds,
											@QueryParam("personId") Integer personId,
											@QueryParam("publicationType") String publicationType,
											@QueryParam("noHighlight") boolean noHighlight,
											@QueryParam("sortByDate") boolean sortByDate,
											@QueryParam("page") Integer page,
											@QueryParam("size") Integer size);

	@GET
	@Path("/{stationId}/postRead")
	ContentResponse<List<PostView>> getPostRead(@PathParam("stationId") Integer stationId,
												@QueryParam("page") Integer page,
												@QueryParam("size") Integer size) throws BadRequestException;

	@GET
	@Path("/{stationId}/allPostRead")
	ContentResponse<List<PostView>> getAllPostRead(@PathParam("stationId") Integer stationId);

	@GET
	@Path("/{stationId}/popular")
	ContentResponse<List<PostView>> getPopular(@PathParam("stationId") Integer stationId,
											   @QueryParam("page") Integer page,
											   @QueryParam("size") Integer size);

	@GET
	@Path("/{stationId}/recent")
	ContentResponse<List<PostView>> getRecent(@PathParam("stationId") Integer stationId,
											  @QueryParam("page") Integer page,
											  @QueryParam("size") Integer size);

	@GET
	@Path("/{postId}/body")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@PostAuthorize("hasPermission(#postId, 'co.xarx.trix.domain.Post', 'read')")
	StringResponse getPostBody(@PathParam("postId") Integer postId);

	@GET
	@Path("/search/findPostsByTags")
	ContentResponse<List<PostView>> findPostsByTagAndStationId(@QueryParam("tags") String tagsString,
															   @QueryParam("stationId") Integer stationId,
															   @QueryParam("page") int page,
															   @QueryParam("size") int size) throws
			ServletException, IOException;

	@GET
	@Path("/search/findPostsByIds")
	ContentResponse<List<PostView>> findPostsByIds(@QueryParam("ids") List<Integer> ids);
}
