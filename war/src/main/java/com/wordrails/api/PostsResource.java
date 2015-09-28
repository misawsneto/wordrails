package com.wordrails.api;

import com.wordrails.PermissionId;
import com.wordrails.WordrailsService;
import com.wordrails.auth.TrixAuthenticationProvider;
import com.wordrails.business.*;
import com.wordrails.business.BadRequestException;
import com.wordrails.converter.PostConverter;
import com.wordrails.persistence.PostRepository;
import com.wordrails.security.PostAndCommentSecurityChecker;
import com.wordrails.util.StationTermsDto;
import com.wordrails.util.WordrailsUtil;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.highlight.*;
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
		if (post == null) {

		}

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
	@Path("/{stationId}/findPostsAndPostsPromotedByBody")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> findPostsAndPostsPromotedByBody(@PathParam("stationId") Integer stationId, @QueryParam("body") String body, @QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {

		Pageable pageable = new PageRequest(page, size);

		body = "%" + body + "%";
		List<Post> posts = postRepository.findPostsAndPostsPromotedByBody(stationId, body, pageable);
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/{stationId}/findPostsByStationIdAndAuthorIdAndState")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> findPostsByStationIdAndAuthorIdAndState(@PathParam("stationId") Integer stationId, @QueryParam("authorId") Integer authorId, @QueryParam("state") String state, @QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {

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
	@Path("/{stationId}/findPostsAndPostsPromotedByTermId")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> findPostsAndPostsPromotedByTermId(@PathParam("stationId") Integer stationId, @QueryParam("termId") Integer termId, @QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {

		Pageable pageable = new PageRequest(page, size);

		List<Post> posts = postRepository.findPostsAndPostsPromotedByTermId(stationId, termId, pageable);
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/{stationId}/findPostsAndPostsPromotedByAuthorId")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> findPostsAndPostsPromotedByAuthorId(@PathParam("stationId") Integer stationId, @QueryParam("authorId") Integer authorId, @QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size);

		List<Post> posts = postRepository.findPostsAndPostsPromotedByAuthorId(stationId, authorId, pageable);
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/{stationId}/searchPostsFromOrPromotedToStation")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<SearchView> searchPostsFromOrPromotedToStation(@Context HttpServletRequest request, @PathParam("stationId") Integer stationId, @QueryParam("query") String q, @QueryParam("stationIds") String stationIds, @QueryParam("personId") Integer personId, @QueryParam("publicationType") Integer publicationType, @QueryParam("noHighlight") Boolean noHighlight, @QueryParam("page") Integer page, @QueryParam("size") Integer size) {

		FullTextEntityManager ftem = org.h3ibernate.search.jpa.Search.getFullTextEntityManager(manager);
		// create native Lucene query unsing the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommend though
		QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Post.class).get();

		Network network = wordrailsService.getNetworkFromHost(request);

		org.apache.lucene.search.Query text = null;
		try {
			if (q != null) {
				text = qb.keyword().fuzzy().withThreshold(.8f).withPrefixLength(1).onField("title").boostedTo(5).andField("body").boostedTo(2).andField("topper").andField("subheading").andField("author.name").andField("terms.name").matching(q).createQuery();
			}
		} catch (Exception e) {

			e.printStackTrace();

			ContentResponse<SearchView> response = new ContentResponse<SearchView>();
			response.content = new SearchView();
			response.content.hits = 0;
			response.content.posts = new ArrayList<PostView>();

			return response;
		}
		;

		MustJunction musts = null;

		if (q != null) {
			musts = qb.bool().must(text);
		}

		if (stationId != null) {
			org.apache.lucene.search.Query station = qb.keyword().onField("stationId").ignoreAnalyzer().matching(stationId).createQuery();
			if (musts == null) musts = qb.bool().must(station);
			else musts.must(station);
		}

		if (personId != null) {
			org.apache.lucene.search.Query person = qb.keyword().onField("author.id").ignoreAnalyzer().matching(personId).createQuery();
			if (musts == null) musts = qb.bool().must(person);
			else musts.must(person);
		}

		org.apache.lucene.search.Query full = musts.createQuery(); //qb.bool().must(text).must(station).createQuery();

		FullTextQuery ftq = ftem.createFullTextQuery(full, Post.class);

		org.apache.lucene.search.Sort sort = new Sort(SortField.FIELD_SCORE, new SortField("id", SortField.INT, true));
		ftq.setSort(sort);

		int totalHits = ftq.getResultSize();

		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query persistenceQuery = ftq;

		// execute search
		List<Post> result = persistenceQuery.setFirstResult(size * page).setMaxResults(size).getResultList();

		List<PostView> postsViews = postConverter.convertToViews(result);

		try {
			Fragmenter fragmenter = new SimpleFragmenter(120);
			Scorer scorer = new QueryScorer(full);
			Encoder encoder = new SimpleHTMLEncoder();
			Formatter formatter = new SimpleHTMLFormatter("<b>", "</b>");
			if (noHighlight != null && noHighlight) formatter = new SimpleHTMLFormatter("", "");

			Highlighter ht = new Highlighter(formatter, encoder, scorer);
			ht.setTextFragmenter(fragmenter);

			Analyzer analyzer = ftem.getSearchFactory().getAnalyzer(Post.class);

			int maxNumFragments = 3;

			for (int i = 0; i < result.size(); i++) {
				Post post = result.get(i);
				String body = Jsoup.parse(post.body).text();
				String[] fragments = ht.getBestFragments(analyzer, "body", body, maxNumFragments);
				if (fragments != null && fragments.length > 0) {
					String snippet = "";
					for (int j = 0; j < fragments.length; j++) {
						snippet = snippet + fragments[j];
						if (j + 1 == fragments.length) break;
						snippet = snippet + "... ";
					}
					postsViews.get(i).snippet = snippet;
				} else {
					postsViews.get(i).snippet = WordrailsUtil.simpleSnippet(body, 100);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}

		ContentResponse<SearchView> response = new ContentResponse<SearchView>();
		response.content = new SearchView();
		response.content.hits = totalHits;
		response.content.posts = postsViews;

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
		Network network = wordrailsService.getNetworkFromHost(request);

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

		FullTextEntityManager ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
		// create native Lucene query unsing the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommend though
		QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Post.class).get();

		org.apache.lucene.search.Query text = null;
		try {
			if (q != null) {
				text = qb.keyword()
						.fuzzy()
						.withThreshold(.8f)
						.withPrefixLength(1)
						.onField("title")
						.boostedTo(5)
						.andField("body")
						.boostedTo(2)
						.andField("topper")
						.andField("subheading")
						.andField("author.name")
						.andField("terms.name")
						.matching(q)
						.createQuery();
			}
		} catch (Exception e) {

			e.printStackTrace();

			ContentResponse<SearchView> response = new ContentResponse<SearchView>();
			response.content = new SearchView();
			response.content.hits = 0;
			response.content.posts = new ArrayList<PostView>();

			return response;
		}
		;

		MustJunction musts = null;

		if (q != null) {
			musts = qb.bool().must(text);
		}

		if (personId != null) {
			org.apache.lucene.search.Query personQ = qb.keyword().onField("author.id").ignoreAnalyzer().matching(personId).createQuery();
			if (musts == null) musts = qb.bool().must(personQ);
			else musts.must(personQ);
		}

		if(publicationType!=null){
			musts = musts.must(qb.keyword().onField("state").ignoreAnalyzer().matching(publicationType).createQuery());
		}else{
			musts = musts.must(qb.keyword().onField("state").ignoreAnalyzer().matching(Post.STATE_PUBLISHED).createQuery());
		}


		BooleanJunction stations = qb.bool();
		for (Integer integer : readableIds) {
			stations.should(qb.keyword().onField("stationId").ignoreAnalyzer().matching(integer).createQuery());
		}

		org.apache.lucene.search.Query full = musts.must(stations.createQuery()).createQuery(); //qb.bool().must(text).must(station).createQuery();

		FullTextQuery ftq = ftem.createFullTextQuery(full, Post.class);

		org.apache.lucene.search.Sort sort = null;
		if(sortByDate != null && sortByDate)
			sort = new Sort(new SortField("date", SortField.STRING, true));
		else
			sort = new Sort(SortField.FIELD_SCORE, new SortField("id", SortField.INT, true));

		ftq.setSort(sort);

		int totalHits = ftq.getResultSize();

		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query persistenceQuery = ftq;

		// execute search
		List<Post> result = persistenceQuery.setFirstResult(size * page).setMaxResults(size).getResultList();

		List<PostView> postsViews = postConverter.convertToViews(result);

		try {
			Fragmenter fragmenter = new SimpleFragmenter(120);
			Scorer scorer = new QueryScorer(full);
			Encoder encoder = new SimpleHTMLEncoder();
			Formatter formatter = new SimpleHTMLFormatter("<b>", "</b>");
			if (noHighlight != null && noHighlight) formatter = new SimpleHTMLFormatter("", "");

			Highlighter ht = new Highlighter(formatter, encoder, scorer);
			ht.setTextFragmenter(fragmenter);

			Analyzer analyzer = ftem.getSearchFactory().getAnalyzer(Post.class);

			int maxNumFragments = 3;

			for (int i = 0; i < result.size(); i++) {
				Post post = result.get(i);
				String body = Jsoup.parse(post.body).text();
				String[] fragments = ht.getBestFragments(analyzer, "body", body, maxNumFragments);
				if (fragments != null && fragments.length > 0) {
					String snippet = "";
					for (int j = 0; j < fragments.length; j++) {
						snippet = snippet + fragments[j];
						if (j + 1 == fragments.length) break;
						snippet = snippet + "... ";
					}
					postsViews.get(i).snippet = snippet;
				} else {
					postsViews.get(i).snippet = WordrailsUtil.simpleSnippet(body, 100);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}

		ContentResponse<SearchView> response = new ContentResponse<SearchView>();
		response.content = new SearchView();
		response.content.hits = totalHits;
		response.content.posts = postsViews;

		return response;
	}

	@GET
	@Path("/{stationId}/postRead")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> getPostRead(@PathParam("stationId") Integer stationId, @QueryParam("page") Integer page, @QueryParam("size") Integer size) throws BadRequestException{

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
	public ContentResponse<List<PostView>> getPopular(@PathParam("stationId") Integer stationId, @QueryParam("page") Integer page, @QueryParam("size") Integer size) {

		Pageable pageable = new PageRequest(page, size);
		List<Post> posts = postRepository.findPopularPosts(stationId, pageable);

		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@GET
	@Path("/{stationId}/recent")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> getRecent(@PathParam("stationId") Integer stationId, @QueryParam("page") Integer page, @QueryParam("size") Integer size) {
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
