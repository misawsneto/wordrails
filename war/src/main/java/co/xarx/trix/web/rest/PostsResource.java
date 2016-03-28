package co.xarx.trix.web.rest;

import co.xarx.trix.api.*;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.QPost;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.services.post.PostService;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Path("/posts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Component
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class PostsResource {

	@Context
	private HttpServletRequest request;
	@Context
	private UriInfo uriInfo;
	@Context
	private HttpServletResponse response;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private SearchResource searchResource;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private PostConverter postConverter;
	@Autowired
	private AuthService authProvider;

	@Autowired
	private PostService postService;

	private void forward() throws ServletException, IOException {
		forward(uriInfo.getPath());
	}

	private void forward(String uri) throws ServletException, IOException {
		String path = request.getServletPath() + uri;
		request.getServletContext().getRequestDispatcher(path).forward(request, response);
	}

	@GET
	@Path("/")
	public void getPosts() throws ServletException, IOException {
		forward();
	}

	@GET
	@Path("/{id}")
	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Post', 'read')")
	public void getPost(@PathParam("id") @P("id") int postId) throws ServletException, IOException {
		forward();
	}

	@PUT
	@Path("/{id}")
	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Post', 'write')")
	public void putPost(@PathParam("id") Integer id) throws ServletException, IOException {
		forward();
	}

	@POST
	@Path("/")
//	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Post', 'write')")
	public void postPost() throws ServletException, IOException {
		forward();
	}

	@DELETE
	@Path("/{id}")
	@PreAuthorize("hasPermission(#id, 'co.xarx.trix.domain.Post', 'write')")
	public void deletePost(@PathParam("id") Integer id) throws ServletException, IOException {
		forward();
	}


	@POST
	@Path("/{postId}/comments")
	@PreAuthorize("hasPermission(#p, 'co.xarx.trix.domain.Post', 'read')")
	public void postComment(@PathParam("postId") @P("p") Integer postId) throws ServletException,
			IOException {
		forward("/comments");
	}


	@GET
	@Path("/{postId}/terms")
	@PreAuthorize("hasPermission(#p, 'co.xarx.trix.domain.Post', 'read')")
	public void getTerms(@PathParam("postId") @P("p") Integer postId) throws ServletException,
			IOException {
		forward("/comments");
	}


	@PUT
	@Path("/{postId}/comments/{commentId}")
	@PreAuthorize("hasPermission(#p, 'co.xarx.trix.domain.Post', 'read')")
	public void putComment(@PathParam("postId") @P("p") Integer postId, @PathParam("commentId") Integer commentId) throws ServletException,
			IOException {
		forward("/comments/" + commentId);
	}

	@PUT
	@Path("/{postId}/updatePostTags")
	@Transactional
	public ContentResponse<PostView> updatePostTerms(@PathParam("postId") Integer postId, List<TermDto> terms) throws ServletException, IOException {

		Post post = postRepository.findOne(postId);

		ContentResponse<PostView> response = new ContentResponse<>();
		response.content = postConverter.convertTo(post);
		return response;
	}

	@GET
	@Path("/getPostViewBySlug")
	@Transactional
	@PostAuthorize("hasPermission(#returnObject.postId, 'co.xarx.trix.domain.Post', 'read') or returnObject==null")
	public PostView getPostViewBySlug(@QueryParam("slug") String slug, @QueryParam("withBody") Boolean withBody) throws ServletException, IOException {
		Post post = postRepository.findBySlug(slug);
		PostView postView = null;
		if (post != null) {
			postView = postConverter.convertTo(post);
			if(withBody != null && withBody)
				postView.body = post.body;
		}

		return postView;
	}

	@GET
	@Path("/{postId}/getPostViewById")
	@Transactional
	@PostAuthorize("hasPermission(#postId, 'co.xarx.trix.domain.Post', 'read')")
	public PostView getPostViewById(@PathParam("postId") Integer postId, @QueryParam("withBody") Boolean withBody) throws ServletException, IOException {
		Post post = postRepository.findOne(postId);
		PostView postView = null;
		if (post != null) {
			postView = postConverter.convertTo(post);
			if(withBody != null && withBody)
				postView.body = post.body;
		}

		return postView;
	}

	@GET
	@Path("/{stationId}/findPostsByStationIdAndAuthorIdAndState")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> findPostsByStationIdAndAuthorIdAndState(@PathParam("stationId") Integer stationId,
																				   @QueryParam("authorId") Integer authorId,
																				   @QueryParam("state") String state,
																				   @QueryParam("page") int page,
																				   @QueryParam("size") int size) throws ServletException, IOException {

		Pageable pageable = new PageRequest(page, size);

		QPost p = QPost.post;

		Page<Post> pagePosts = postRepository.findAll(p.station.id.eq(stationId).and(p.author.id.eq(authorId)), pageable);
		List<Post> posts = Lists.newArrayList(pagePosts.iterator());

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@Deprecated
	@GET
	@Path("/search/networkPosts")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<SearchView> searchPosts(@Context HttpServletRequest request,
												   @QueryParam("query") String q,
												   @QueryParam("stationIds") String stationIds,
												   @QueryParam("personId") Integer personId,
												   @QueryParam("publicationType") String publicationType,
												   @QueryParam("noHighlight") boolean noHighlight,
												   @QueryParam("sortByDate") boolean sortByDate,
												   @QueryParam("page") Integer page,
												   @QueryParam("size") Integer size) {

		return searchResource.posts(q, page, size, personId, sortByDate);
	}

	@GET
	@Path("/{stationId}/postRead")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> getPostRead(@PathParam("stationId") Integer stationId,
													   @QueryParam("page") Integer page,
													   @QueryParam("size") Integer size) throws BadRequestException{

		if (stationId == null || page == null || size == null) {
			throw new BadRequestException("Invalid null parameter(s).");
		}

		Person person = authProvider.getLoggedPerson();
		Pageable pageable = new PageRequest(page, size);
		List<Post> posts = postRepository.findPostReadByStationAndPerson(stationId, person.id, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/{stationId}/allPostRead")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> getAllPostRead(@PathParam("stationId") Integer stationId) {

		Person person = authProvider.getLoggedPerson();
		List<Post> posts = postRepository.findPostReadByStationAndPerson(stationId, person.id);

		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/{stationId}/popular")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> getPopular(@PathParam("stationId") Integer stationId,
													  @QueryParam("page") Integer page,
													  @QueryParam("size") Integer size) {

		Pageable pageable = new PageRequest(page, size);
		List<Post> posts = postRepository.findPopularPosts(stationId, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/{stationId}/recent")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> getRecent(@PathParam("stationId") Integer stationId,
													 @QueryParam("page") Integer page,
													 @QueryParam("size") Integer size) {
		Pageable pageable = new PageRequest(page, size);
		List<Post> posts = postRepository.findPostsOrderByDateDesc(stationId, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/{postId}/body")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@PostAuthorize("hasPermission(#postId, 'co.xarx.trix.domain.Post', 'read')")
	public StringResponse getPostBody(@PathParam("postId") Integer postId){
		Person person = authProvider.getLoggedPerson();
		String body = postRepository.findPostBodyById(postId);
		Post post = postRepository.findOne(postId);

		String requestedSessionId = request.getRequestedSessionId();
		postService.countPostRead(post.id, person.id, requestedSessionId);

		StringResponse content = new StringResponse();
		content.response = body;
		return content;
	}

	@GET
	@Path("/search/findPostsByTags")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> findPostsByTagAndStationId(@QueryParam("tags") String tagsString,
																	  @QueryParam("stationId") Integer stationId,
																	  @QueryParam("page") int page,
																	  @QueryParam("size") int size) throws ServletException, IOException {
		if (tagsString == null || tagsString.isEmpty()) {
			throw new BadRequestException("Tags list is empty or null");
		}

		Set<String> tags = new HashSet<String>(Arrays.asList(tagsString.split(",")));

		List<Post> posts;

		if(stationId == null){
			posts = queryPersistence.findPostsByTag(tags, page, size);
		} else {
			posts = queryPersistence.findPostsByTagAndStationId(tags, stationId, page, size);
		}

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}
}
