package co.xarx.trix.web.rest;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.auth.TrixAuthenticationProvider;
//import org.hibernate.search.jpa.FullTextEntityManager;
//import org.hibernate.search.jpa.FullTextQuery;
//import org.hibernate.search.query.dsl.QueryBuilder;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Recommend;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.persistence.RecommendRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Path("/recommends")
@Consumes(MediaType.WILDCARD)
@Component
public class RecommendsResource {
	private @Context HttpServletRequest request;
	private @Context HttpServletResponse response;

	private @Autowired PostRepository postRepository;
	private @Autowired PostConverter postConverter;
	private @Autowired RecommendRepository recommendRepository;
	private @Autowired
	TrixAuthenticationProvider authProvider;
	private @Autowired QueryPersistence queryPersistence;

	private @PersistenceContext EntityManager manager;

//	@GET
//	@Path("/searchRecommends")
//	@Produces(MediaType.APPLICATION_JSON)
//	public ContentResponse<List<PostView>> searchRecommends(@QueryParam("query") String q, @QueryParam("page") Integer page, @QueryParam("size") Integer size){
//
//		Person person = authProvider.getLoggedPerson();
//
//		if(q == null || q.trim().isEmpty()){
//			Pageable pageable = new PageRequest(page, size);
//
//			ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
//			List<Recommend> pages = recommendRepository.findRecommendsByPersonIdOrderByDate(person.id, pageable);
//
//			List<PostView> recommends = new ArrayList<PostView>();
//			for (Recommend recommend : pages) {
//				recommends.add(postConverter.convertToView(recommend.post));
//			}
//			response.content = recommends;
//			return response;
//		}
//
//		FullTextEntityManager ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
//		// create native Lucene query unsing the query DSL
//		// alternatively you can write the Lucene query using the Lucene query parser
//		// or the Lucene programmatic API. The Hibernate Search DSL is recommend though
//		QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Recommend.class).get();
//
//		org.apache.lucene.search.Query text = null;
//		try{
//
//			text = qb.keyword()
//				.fuzzy()
//				.withThreshold(.8f)
//				.withPrefixLength(1)
//				.onField("post.title").boostedTo(5)
//				.andField("post.body").boostedTo(2)
//				.andField("post.topper")
//				.andField("post.subheading")
//				.andField("post.author.name")
//				.andField("post.terms.name")
//				.matching(q).createQuery();
//		}catch(Exception e){
//
//			e.printStackTrace();
//
//			ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
//			response.content = new ArrayList<PostView>();
//
//			return response;
//		};
//
//		org.apache.lucene.search.Query personQuery = qb.keyword().onField("person.id").ignoreAnalyzer().matching(person.id).createQuery();
//
//		org.apache.lucene.search.Query full = qb.bool().must(text).must(personQuery).createQuery();
//
//		FullTextQuery ftq = ftem.createFullTextQuery(full, Recommend.class);
//
//		// wrap Lucene query in a javax.persistence.Query
//		javax.persistence.Query persistenceQuery = ftq;
//
//		// execute search
//		List<Recommend> result = persistenceQuery
//				.setFirstResult(size * page)
//				.setMaxResults(size)
//				.getResultList();
//
//		List<PostView> recommends = new ArrayList<PostView>();
//		for (Recommend recommend : result) {
//			recommends.add(postConverter.convertToView(recommend.post));
//		}
//
//		ContentResponse<List<PostView>> response = new ContentResponse<List<PostView>>();
//		response.content = recommends;
//
//		return response;
//	}

	@PUT
	@Path("/toggleRecommend")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public BooleanResponse toggleRecommend(@FormParam("postId") Integer postId){

		Person person = authProvider.getLoggedPerson();
		if(person == null || person.username.equals("wordrails"))
			throw new UnauthorizedException();
		
		Recommend recommend = new Recommend();
		recommend.post = postRepository.findOne(postId);
		recommend.person = person;
		try{
			recommendRepository.save(recommend);
			BooleanResponse content = new BooleanResponse();
			content.response = true;
			queryPersistence.incrementRecommendsCount(postId);
			return content;
		}catch(Exception e){
			BooleanResponse content = new BooleanResponse();
			queryPersistence.deleteRecommend(postId, person.id);
			content = new BooleanResponse();
			content.response = false;
			queryPersistence.decrementRecommendsCount(postId);
			return content;
		}
	}
	
}