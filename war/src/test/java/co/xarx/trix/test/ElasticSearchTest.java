package co.xarx.trix.test;

import co.xarx.trix.api.PostView;
import co.xarx.trix.config.spring.ElasticSearchConfig;
import co.xarx.trix.config.spring.PropertyConfig;
import co.xarx.trix.domain.Post;
import co.xarx.trix.elasticsearch.ESPostRepository;
import co.xarx.trix.test.config.ApplicationTestConfig;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		ApplicationTestConfig.class,
		PropertyConfig.class,
		ElasticSearchConfig.class
})
public class ElasticSearchTest {

//	@Autowired
//	private ElasticsearchTemplate template;
	@Autowired
	private ESPostRepository esPostRepository;


	@Test
	public void testPostIndex() throws Exception {
		PostView post = new PostView();
		post.postId = 3;
		post.title = "title";
		post.tenantId = "demo";

		PostView post2 = new PostView();
		post2.postId = 4;
		post2.title = "title2";
		post2.tenantId = "demo";

		esPostRepository.index(post);
		esPostRepository.index(post2);

		NativeSearchQuery nativeSearchQuery = new NativeSearchQuery(QueryBuilders.matchAllQuery());
		nativeSearchQuery.addIndices("demo");
		esPostRepository.findAll().iterator().forEachRemaining(postView -> System.out.println(postView.title));
	}

	@Test
	public void testExpression() throws Exception {
		Post post = new Post();
		post.title = "title";
		post.setTenantId("demo");
		post.setNetworkId(5);

		SpelExpressionParser parser = new SpelExpressionParser();
		Expression expression = parser.parseExpression("#{getTenantId()}", ParserContext.TEMPLATE_EXPRESSION);
		EvaluationContext context = new StandardEvaluationContext(post);
		assertEquals(expression.getValue(context, String.class), "demo");
	}
}
