package com.wordrails.api;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.search.highlight.Encoder;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLEncoder;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.WordrailsService;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.BadRequestException;
import com.wordrails.business.Person;
import com.wordrails.business.Post;
import com.wordrails.converter.PostConverter;
import com.wordrails.persistence.PostRepository;
import com.wordrails.util.WordrailsUtil;

@Path("/posts")
@Consumes(MediaType.WILDCARD)
@Component
public class PostsResource {
	private @Context HttpServletRequest request;
	private @Context UriInfo uriInfo;
	private @Context HttpServletResponse response;

	private @Autowired WordrailsService wordrailsService;
	private @Autowired PostRepository postRepository;
	private @Autowired PostConverter postConverter;

	private @PersistenceContext EntityManager manager;
	
	private @Autowired AccessControllerUtil accessControllerUtil;

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
		if(post == null){
			
		}
		
		ContentResponse<PostView> response = new ContentResponse<PostView>();
		response.content = postConverter.convertToView(post); 
		return response;
	}
	
	@GET
	@Path("/{postId}/getPostView")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Transactional
	public ContentResponse<PostView> getPostView(@PathParam("postId") Integer postId, @QueryParam("withBody") boolean withBody) throws ServletException, IOException {
		Post post = postRepository.findOne(postId);
		ContentResponse<PostView> response = new ContentResponse<PostView>();
		if(post != null){
			response.content = postConverter.convertToView(post); 
			response.content.snippet = post.body;
		}
		
		return response;
	}
	@GET
	@Path("/{postId}")
	public void getPost(@PathParam("postId") int postId) throws ServletException, IOException {
		String userIp = request.getRemoteAddr();
		String username = SecurityContextHolder.getContext().getAuthentication().getName();
//		analytics.postViewed(username, userIp, postId);
		forward();
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
	public ContentResponse<List<PostView>> findPostsAndPostsPromotedByBody(@PathParam("stationId") Integer stationId, @QueryParam("body") String body, 
			@QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {

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
	public ContentResponse<List<PostView>> findPostsByStationIdAndAuthorIdAndState(@PathParam("stationId") Integer stationId, @QueryParam("authorId") Integer authorId, @QueryParam("state") String state, 
			@QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
		
		Pageable pageable = new PageRequest(page, size);
		
		List<Post> posts = postRepository.findPostsByStationIdAndAuthorIdAndState(stationId, authorId, state, pageable);
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts); 
		return response;
	}

	@GET
	@Path("/{stationId}/findPostsAndPostsPromotedByTermId")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> findPostsAndPostsPromotedByTermId(@PathParam("stationId") Integer stationId, @QueryParam("termId") Integer termId, 
			@QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {

		Pageable pageable = new PageRequest(page, size);

		List<Post> posts = postRepository.findPostsAndPostsPromotedByTermId(stationId, termId, pageable);
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts); 
		return response;
	}

	@GET
	@Path("/{stationId}/findPostsAndPostsPromotedByAuthorId")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> findPostsAndPostsPromotedByAuthorId(@PathParam("stationId") Integer stationId, @QueryParam("authorId") Integer authorId, 
			@QueryParam("page") int page, @QueryParam("size") int size) throws ServletException, IOException {
		Pageable pageable = new PageRequest(page, size);

		List<Post> posts = postRepository.findPostsAndPostsPromotedByAuthorId(stationId, authorId, pageable);
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts); 
		return response;
	}
	
	@GET
	@Path("/{stationId}/searchPostsFromOrPromotedToStation")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<SearchView> searchPostsFromOrPromotedToStation(@PathParam("stationId") Integer stationId, @QueryParam("query") String q, @QueryParam("page") Integer page, @QueryParam("size") Integer size){
		FullTextEntityManager ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
		// create native Lucene query unsing the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommend though
		QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Post.class).get();

		org.apache.lucene.search.Query text = qb.keyword()
				.fuzzy()
				.withThreshold(.8f)
				.withPrefixLength(1)
				.onField("title").boostedTo(5)
				.andField("body").boostedTo(2)
				.andField("terms.name")
				.matching(q).createQuery();

		org.apache.lucene.search.Query station = qb.keyword().onField("station.id").ignoreAnalyzer().matching(stationId).createQuery();

		org.apache.lucene.search.Query full = qb.bool().must(text).must(station).createQuery();

		FullTextQuery ftq = ftem.createFullTextQuery(full, Post.class);
		int totalHits = ftq.getResultSize();

		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query persistenceQuery = ftq;

		// execute search
		List<Post> result = persistenceQuery
				.setFirstResult(size * page)
				.setMaxResults(size)
				.getResultList();

		List<PostView> postsViews = postConverter.convertToViews(result);
		try {
			Fragmenter fragmenter = new SimpleFragmenter(120); 
			Scorer scorer = new QueryScorer(full); 
			Encoder encoder = new SimpleHTMLEncoder(); 
			Formatter formatter = new SimpleHTMLFormatter("<b>", "</b>"); 

			Highlighter ht = new Highlighter(formatter, encoder, scorer); 
			ht.setTextFragmenter(fragmenter);

			Analyzer analyzer = ftem.getSearchFactory().getAnalyzer(Post.class);
			
			int maxNumFragments = 3;
			
			for(int i = 0; i < result.size();i ++){
				Post post = result.get(i);
				String body = Jsoup.parse(post.body).text();
				String[] fragments = ht.getBestFragments(analyzer, "body", body, maxNumFragments);
				if(fragments != null && fragments.length > 0){
					String snippet = "";
					for (int j = 0; j < fragments.length ; j++ ) {
						snippet = snippet + fragments[j];
						if(j + 1 == fragments.length)
							break;
						snippet = snippet + "... ";
					}
					postsViews.get(i).snippet = snippet;
				}else{
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
	@Path("/{stationId}/unread")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> getUnread(@PathParam("stationId") Integer stationId, @QueryParam("page") Integer page, @QueryParam("size") Integer size){
		
		if(stationId == null || page == null || size == null){
			throw new BadRequestException("Invalid null parameter(s).");
		}
		
		Person person = accessControllerUtil.getLoggedPerson();
		Pageable pageable = new PageRequest(page, size);
		List<Post> posts = postRepository.findUnreadByStationAndPerson(stationId, person.id, pageable);
		 
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}
	
	@GET
	@Path("/{stationId}/allUnread")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> getAllUnread(@PathParam("stationId") Integer stationId){
		
		Person person = accessControllerUtil.getLoggedPerson();
		List<Post> posts = postRepository.findUnreadByStationAndPerson(stationId, person.id);
		 
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}
	
	@GET
	@Path("/{stationId}/popular")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> getPopular(@PathParam("stationId") Integer stationId, @QueryParam("page") Integer page, @QueryParam("size") Integer size){

		Pageable pageable = new PageRequest(page, size);
		List<Post> posts = postRepository.findPopularPosts(stationId, pageable);
		 
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}
	
	@GET
	@Path("/{stationId}/recent")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> getRecent(@PathParam("stationId") Integer stationId, @QueryParam("page") Integer page, @QueryParam("size") Integer size){
		Pageable pageable = new PageRequest(page, size);
		List<Post> posts = postRepository.findPostsOrderByDateDesc(stationId, pageable);
		 
		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

}