package com.wordrails.api;

import com.wordrails.business.Post;
import com.wordrails.test.AbstractTest;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.joda.time.DateTime;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;


@Component
@TransactionConfiguration
@Transactional
public class PersonsResourceTest extends AbstractTest {

	private
	@PersistenceContext
	EntityManager manager;
	@Autowired PersonsResource personsResource;

	@Test
	public void testSearchStats() throws Exception {

		try {
			personsResource.personStats("", null);
		}catch (Exception e){
			e.printStackTrace();
		}

//		Person person = authProvider.getLoggedPerson();
//		String baseUrl = "http://localhost:8080";
//		Network network = wordrailsService.getNetworkFromHost(request);
//		PermissionId pId = new PermissionId();
//		pId.baseUrl = baseUrl;
//		pId.networkId = network.id;
//		pId.personId = person.id;
//
//		StationsPermissions permissions = new StationsPermissions();
//		try {
//			permissions = wordrailsService.getPersonPermissions(pId);
//		} catch (ExecutionException e1) {
//			e1.printStackTrace();
//		}
//
//		List<Integer> readableIds = wordrailsService.getReadableStationIds(permissions);
//
//		FullTextEntityManager ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
//		QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Post.class).get();
//
//		String q = "aegyusaoehayueogas";
//
//		//org.apache.lucene.search.Query t1 = qb.keyword().fuzzy().onField("id").ignoreAnalyzer().matching(1633).createQuery();
//		org.apache.lucene.search.Query t1 = qb.keyword().fuzzy().withThreshold(.8f).withPrefixLength(1).onField("title").
//				boostedTo(5).andField("body").boostedTo(2).andField("topper").andField("subheading").andField("author.name").
//				andField("terms.name").ignoreAnalyzer().matching(q).createQuery();
//
//		javax.persistence.Query persistenceQuery = ftem.createFullTextQuery(t1, Post.class);
//		List<Post> posts = persistenceQuery.getResultList();
//		for(Post p : posts) {
//			System.out.println(p.title + " : " + p.body);
//		}
//		System.out.println(ftem.createFullTextQuery(t1).getResultSize());
	}
}