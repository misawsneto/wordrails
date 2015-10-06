package com.wordrails.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wordrails.PermissionId;
import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.*;
import com.wordrails.business.BadRequestException;
import com.wordrails.converter.PostConverter;
import com.wordrails.elasticsearch.PostEsRepository;
import com.wordrails.elasticsearch.PostIndexed;
import com.wordrails.persistence.PostRepository;
import com.wordrails.security.PostAndCommentSecurityChecker;
import com.wordrails.util.StationTermsDto;
import com.wordrails.util.WordrailsUtil;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.highlight.*;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.lucene.Lucene;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.multiMatchQuery;

@Path("/posts")
@Consumes(MediaType.WILDCARD)
@Component
public class PostsResource {
	private
	@Context
	HttpServletRequest request;
	private
	@Context
	UriInfo uriInfo;
	private
	@Context
	HttpServletResponse response;

	private
	@Autowired
	WordrailsService wordrailsService;
	private
	@Autowired
	PostRepository postRepository;
	private
	@Autowired
	PostConverter postConverter;
	
	private
	@Autowired
	PostAndCommentSecurityChecker postAndCommentSecurityChecker;

	private
	@PersistenceContext
	EntityManager manager;

	private
	@Autowired
	TrixAuthenticationProvider authProvider;

	private
	@Autowired
	PostEsRepository postEsRepository;

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

		ContentResponse<PostView> response = new ContentResponse<PostView>();
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
			ContentResponse<PostView> response = new ContentResponse<PostView>();
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

		MultiMatchQueryBuilder queryText = null;
		BoolQueryBuilder mainQuery = boolQuery();

		if(q != null){
			queryText = multiMatchQuery(q)
					.field("post.snippet", 2)
					.field("post.title", 5)
					.field("post.topper")
					.field("post.subheading")
					.field("post.authorName")
					.field("post.terms")
					.prefixLength(1)
					.fuzziness(Fuzziness.AUTO);
		} else {
			ContentResponse<SearchView> response = new ContentResponse<SearchView>();
			response.content = new SearchView();
			response.content.hits = 0;
			response.content.posts = new ArrayList<JSONObject>();

			return response;
		}

		mainQuery = mainQuery.must(queryText);

		if(personId != null){
			mainQuery = mainQuery.must(
					matchQuery("post.authorId", personId));
		}

		if(publicationType != null){
			mainQuery = mainQuery.must(
					matchQuery("post.state", publicationType));
		} else {
			mainQuery = mainQuery.must(
					matchQuery("post.state", Post.STATE_PUBLISHED));
		}

		BoolQueryBuilder statiosQuery = boolQuery();
		for(Integer stationId: readableIds){
			statiosQuery.should(
					matchQuery("post.stationId", String.valueOf(stationId)));
		}
		mainQuery = mainQuery.must(statiosQuery);
		FieldSortBuilder sort = null;

		if(sortByDate != null && sortByDate){
			sort = new FieldSortBuilder("post.date")
					.order(SortOrder.DESC);
		}

		SearchResponse searchResponse = postEsRepository.runQuery(mainQuery.toString(), sort, size, page, true);

		SearchHit[] resultList = searchResponse.getHits().getHits();
		List<JSONObject> postsViews = new ArrayList<>();

		for(SearchHit hit: resultList){
			try {
				postsViews.add(postEsRepository.convertToView(hit.getSourceAsString()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		ContentResponse<SearchView> response = new ContentResponse<SearchView>();
		response.content = new SearchView();
		response.content.hits = resultList.length;
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
