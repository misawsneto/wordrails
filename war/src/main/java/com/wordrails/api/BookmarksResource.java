package com.wordrails.api;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import com.wordrails.WordrailsService;
import com.wordrails.business.AccessControllerUtil;
import com.wordrails.business.Bookmark;
import com.wordrails.business.Person;
import com.wordrails.business.UnauthorizedException;
import com.wordrails.converter.PostConverter;
import com.wordrails.persistence.BookmarkRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.QueryPersistence;

@Path("/bookmarks")
@Consumes(MediaType.WILDCARD)
@Component
public class BookmarksResource {
	private @Context HttpServletRequest request;
	private @Context UriInfo uriInfo;
	private @Context HttpServletResponse response;

	private @Autowired WordrailsService wordrailsService;
	private @Autowired PostRepository postRepository;
	private @Autowired PostConverter postConverter;
	private @Autowired BookmarkRepository bookmarkRepository;
	private @Autowired AccessControllerUtil accessControllerUtil;
	private @Autowired QueryPersistence queryPersistence;
	
	private @PersistenceContext EntityManager manager;

	@GET
	@Path("/searchBookmarks")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> searchBookmarks(@QueryParam("query") String q, @QueryParam("page") Integer page, @QueryParam("size") Integer size){
		
		Person person = accessControllerUtil.getLoggedPerson();
		
		if(q == null || q.trim().isEmpty()){
			Pageable pageable = new PageRequest(page, size);
			
			ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
			List<Bookmark> pages = bookmarkRepository.findBookmarksByPersonIdOrderByDate(person.id, pageable);
			
			List<PostView> bookmarks = new ArrayList<PostView>();
			for (Bookmark bookmark : pages) {
				bookmarks.add(postConverter.convertToView(bookmark.post));
			}
			response.content = bookmarks;
			return response;
		}
		
		FullTextEntityManager ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
		// create native Lucene query unsing the query DSL
		// alternatively you can write the Lucene query using the Lucene query parser
		// or the Lucene programmatic API. The Hibernate Search DSL is recommend though
		QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Bookmark.class).get();

		org.apache.lucene.search.Query text = null;
		try{
			
			text = qb.keyword()
				.fuzzy()
				.withThreshold(.8f)
				.withPrefixLength(1)
				.onField("post.title").boostedTo(5)
				.andField("post.body").boostedTo(2)
				.andField("post.topper")
				.andField("post.subheading")
				.andField("post.author.name")
				.andField("post.terms.name")
				.matching(q).createQuery();
		}catch(Exception e){
			
			e.printStackTrace();
			
			ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
			response.content = new ArrayList<PostView>();

			return response;
		};
		
		org.apache.lucene.search.Query personQuery = qb.keyword().onField("person.id").ignoreAnalyzer().matching(person.id).createQuery();

		org.apache.lucene.search.Query full = qb.bool().must(text).must(personQuery).createQuery();
		
		FullTextQuery ftq = ftem.createFullTextQuery(full, Bookmark.class);

		// wrap Lucene query in a javax.persistence.Query
		javax.persistence.Query persistenceQuery = ftq;

		// execute search
		List<Bookmark> result = persistenceQuery
				.setFirstResult(size * page)
				.setMaxResults(size)
				.getResultList();

		List<PostView> bookmarks = new ArrayList<PostView>();
		for (Bookmark bookmark : result) {
			bookmarks.add(postConverter.convertToView(bookmark.post));
		}

		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
		response.content = bookmarks;

		return response;
	}
	
	@PUT
	@Path("/toggleBookmark")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public ContentResponse<BooleanResponse> toggleBookmark(@FormParam("postId") Integer postId){
		
		Person person = accessControllerUtil.getLoggedPerson();
		if(person.username.equals("wordrails"))
			throw new UnauthorizedException();
		
		Bookmark bookmark = new Bookmark();
		bookmark.post = postRepository.findOne(postId);
		bookmark.person = person;
		ContentResponse<BooleanResponse> bool = new ContentResponse<BooleanResponse>();
		try{
			bookmarkRepository.save(bookmark);
			bool.content = new BooleanResponse();
			bool.content.response = true;
			queryPersistence.incrementBookmarksCount(postId);
			return bool;
		}catch(Exception e){
			queryPersistence.decrementBookmarksCount(postId);
			bool.content = new BooleanResponse();
			bool.content.response = false;
			queryPersistence.deleteBookmark(postId, person.id);
			return bool;
		}
	}
	
}