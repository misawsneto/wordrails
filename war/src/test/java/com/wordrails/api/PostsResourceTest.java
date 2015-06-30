package com.wordrails.api;

import com.wordrails.business.Post;
import com.wordrails.test.AbstractTest;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.Test;
import org.springframework.stereotype.Component;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.Assert.*;


@Component
@TransactionConfiguration
@Transactional
public class PostsResourceTest extends AbstractTest {

	private
	@PersistenceContext
	EntityManager manager;

	@Test
	public void testSearchPosts() throws Exception {
		FullTextEntityManager ftem = org.hibernate.search.jpa.Search.getFullTextEntityManager(manager);
		QueryBuilder qb = ftem.getSearchFactory().buildQueryBuilder().forEntity(Post.class).get();


		org.apache.lucene.search.Query t1 = qb.keyword().fuzzy().onField("id").ignoreAnalyzer().matching(1633).createQuery();
		List<Post> posts = ftem.createFullTextQuery(t1).getResultList();
		for(Post p : posts) {
			System.out.println(p.body);
		}
		System.out.println(ftem.createFullTextQuery(t1).getResultSize());
	}
}