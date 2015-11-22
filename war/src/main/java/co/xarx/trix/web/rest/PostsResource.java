package co.xarx.trix.web.rest;

import co.xarx.trix.PermissionId;
import co.xarx.trix.WordrailsService;
import co.xarx.trix.api.*;
import co.xarx.trix.auth.TrixAuthenticationProvider;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.dto.StationTermsDto;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.elasticsearch.PostEsRepository;
import co.xarx.trix.security.PostAndCommentSecurityChecker;
import co.xarx.trix.services.PostService;
import co.xarx.trix.util.TrixUtil;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Path("/posts")
@Consumes(MediaType.WILDCARD)
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
	private WordrailsService wordrailsService;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostConverter postConverter;
	@Autowired
	private PostAndCommentSecurityChecker postAndCommentSecurityChecker;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private PostEsRepository postEsRepository;
	@Autowired
	private ObjectMapper objectMapper;

	private void forward() throws ServletException, IOException {
		String path = request.getServletPath() + uriInfo.getPath();
		request.getServletContext().getRequestDispatcher(path).forward(request, response);
	}


	@PUT
	@Path("/{postId}/updatePostTags")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public ContentResponse<PostView> updatePostTerms(@PathParam("postId") Integer postId, List<TermDto> terms) throws ServletException, IOException {

		Post post = postRepository.findOne(postId);

		ContentResponse<PostView> response = new ContentResponse<>();
		response.content = postConverter.convertToView(post);
		return response;
	}

	@GET
	@Path("/getPostViewBySlug")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public PostView getPostViewBySlug(@QueryParam("slug") String slug, @QueryParam("withBody") Boolean withBody) throws ServletException, IOException {
		Post post = postRepository.findBySlug(slug);
		PostView postView = null;
		if (post != null) {
			postView = postConverter.convertToView(post);
			if(withBody != null && withBody)
				postView.body = post.body;
		}

		return postView;
	}

	@GET
	@Path("/{postId}/getPostViewById")
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public PostView getPostViewById(@PathParam("postId") Integer postId, @QueryParam("withBody") Boolean withBody) throws ServletException, IOException {
		Post post = postRepository.findOne(postId);
		PostView postView = null;
		if (post != null) {
			postView = postConverter.convertToView(post);
			if(withBody != null && withBody)
				postView.body = post.body;
		}

		return postView;
	}

	@GET
	@Path("/{postId}")
	public void getPost(@PathParam("postId") int postId) throws ServletException, IOException {
		String userIp = request.getRemoteAddr();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
//		analytics.postViewed(username, userIp, postId);
		forward();
	}

	@Autowired
	private PostService postService;

	@PUT
	@Path("/{postId}/convert")
	@Transactional(readOnly=false)
	public ContentResponse<PostView> convertPost(@PathParam("postId") int postId, @FormParam("state") String state) throws ServletException, IOException {
		Post post = postRepository.findOne(postId);
		if(post != null && postAndCommentSecurityChecker.canWrite(post)){
			post = postService.convertPost(postId, state);
			ContentResponse<PostView> response = new ContentResponse<>();
			response.content = postConverter.convertToView(post);
			return response;
		}else{
			throw new UnauthorizedException();
		}
		
	}

	@PUT
	@Path("/{id}")
	public void putPost(@PathParam("id") Integer id) throws ServletException, IOException {
		forward();
	}

	@DELETE
	@Path("/{id}")
	public void deletePost(@PathParam("id") Integer id) throws ServletException, IOException {
		forward();
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

		List posts = null;
		if (state.equals("PUBLISHED"))
			posts = postRepository.findPostsByStationIdAndAuthorId(stationId, authorId, pageable);
		if (state.equals("DRAFT"))
			posts = postRepository.findDraftsByStationIdAndAuthorId(stationId, authorId, pageable);
		if (state.equals("SCHEDULED"))
			posts = postRepository.findScheduledsByStationIdAndAuthorId(stationId, authorId, pageable);
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/search/networkPosts")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<SearchView> searchPosts(@Context HttpServletRequest request,
	                                               @QueryParam("query") String q,
	                                               @QueryParam("stationIds") String stationIds,
	                                               @QueryParam("personId") Integer personId,
	                                               @QueryParam("publicationType") String publicationType,
	                                               @QueryParam("noHighlight") Boolean noHighlight,
	                                               @QueryParam("sortByDate") Boolean sortByDate,
	                                               @QueryParam("page") Integer page,
	                                               @QueryParam("size") Integer size) {

		List<Integer> stationIdIntegers = new ArrayList<Integer>();

		if(stationIds != null){
			List<String> stringIds = Arrays.asList(stationIds.replaceAll("\\s*", "").split(","));
			for (String id: stringIds)
				stationIdIntegers.add(Integer.parseInt(id));
		}

		Person person = authProvider.getLoggedPerson();
		String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
		Network network = wordrailsService.getNetworkFromHost(request.getHeader("Host"));

		PermissionId pId = new PermissionId();
		pId.baseUrl = baseUrl;
		pId.networkId = network.id;
		pId.personId = person.id;

		StationsPermissions permissions = new StationsPermissions();
		try {
			permissions = wordrailsService.getPersonPermissions(pId);
		} catch (ExecutionException e1) {
			e1.printStackTrace();
		}

		List<Integer> readableIds = wordrailsService.getReadableStationIds(permissions);

		MultiMatchQueryBuilder queryText;
		BoolQueryBuilder mainQuery = boolQuery();

		if(q != null){
			queryText = multiMatchQuery(q)
					.field("body", 2)
					.field("title", 5)
					.field("topper")
					.field("subheading")
					.field("authorName")
					.field("terms.name")
					.prefixLength(1)
					//.fuzziness(Fuzziness.AUTO)
					;
		} else {
			ContentResponse<SearchView> response = new ContentResponse<SearchView>();
			response.content = new SearchView();
			response.content.hits = 0;
			response.content.posts = new ArrayList<PostView>();

			return response;
		}

		mainQuery = mainQuery.must(queryText);

		if(personId != null){
			mainQuery = mainQuery.must(
					matchQuery("authorId", personId));
		}

		if(publicationType != null){
			mainQuery = mainQuery.must(
					matchQuery("state", publicationType));
		} else {
			mainQuery = mainQuery.must(
					matchQuery("state", Post.STATE_PUBLISHED));
		}

		if(stationIdIntegers.size() > 0)
			readableIds = stationIdIntegers;

		BoolQueryBuilder stationQuery = boolQuery();
		for(Integer stationId: readableIds){
			stationQuery.should(
					matchQuery("stationId", String.valueOf(stationId)));
		}
		mainQuery = mainQuery.must(stationQuery);
		FieldSortBuilder sort = null;

		if(sortByDate != null && sortByDate){
			sort = new FieldSortBuilder("date")
					.order(SortOrder.DESC);

		}

		SearchResponse searchResponse = postEsRepository.runQuery(mainQuery.toString(), sort, size, page, "body");

		List<PostView> postsViews = new ArrayList<>();

		for(SearchHit hit: searchResponse.getHits().getHits()){
			try {
				PostView postView = objectMapper.readValue(hit.getSourceAsString(), PostView.class);
				Map<String, HighlightField> highlights = hit.getHighlightFields();
				if(highlights != null && highlights.get("body") != null)
					for (Text fragment:  highlights.get("body").getFragments()) {
						if(postView.snippet == null)
							postView.snippet = "";
						postView.snippet = postView.snippet + " " + fragment.toString();
					}
				else{
					postView.snippet = TrixUtil.simpleSnippet(postView.body, 100);
				}

				postView.snippet = TrixUtil.htmlStriped(postView.snippet);
				postView.snippet = postView.snippet.replaceAll("\\{snippet\\}", "<b>").replaceAll("\\{#snippet\\}", "</b>");
				postsViews.add(postView);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ContentResponse<SearchView> response = new ContentResponse<>();
		response.content = new SearchView();
		response.content.hits = (int) searchResponse.getHits().totalHits();
		response.content.posts = postsViews;

		return response;

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
	public StringResponse getPostBody(@PathParam("postId") Integer postId){
		Person person = authProvider.getLoggedPerson();
		String body = postRepository.findPostBodyById(postId);

		wordrailsService.countPostRead(postId, person, request.getRequestedSessionId());

		StringResponse content = new StringResponse();
		content.response = body;
		return content;
	}

	@PUT
	@Path("/{postId}")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public Response promote(@PathParam("postId") Integer postId, StationTermsDto stationTerms){
		return Response.status(Status.OK).build();
	}
}
