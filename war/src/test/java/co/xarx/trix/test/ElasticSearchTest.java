package co.xarx.trix.test;

import co.xarx.trix.config.spring.ApplicationConfig;
import co.xarx.trix.domain.Post;
import co.xarx.trix.services.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
		ElasticSearchTest.ApplicationTestConfig.class
})
@ActiveProfiles("dev")
public class ElasticSearchTest {

	@Configuration
	@Import({ApplicationConfig.class})
	@ComponentScan(basePackages = {"co.xarx.trix.elasticsearch"})
	public static class ApplicationTestConfig {
	}

//	@Autowired
//	private ElasticsearchTemplate template;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private PostService postService;


	@Test
	public void testPostIndex() throws Exception {
		Post post = TestArtifactsFactory.createPost();

		postService.saveIndex(post);
	}

//	@Test
//	public void testExpression() throws Exception {
//		Post post = new Post();
//		post.title = "title";
//		post.setTenantId("demo");
//		post.setNetworkId(5);
//
//		SpelExpressionParser parser = new SpelExpressionParser();
//		Expression expression = parser.parseExpression("#{getTenantId()}", ParserContext.TEMPLATE_EXPRESSION);
//		EvaluationContext context = new StandardEvaluationContext(post);
//		assertEquals(expression.getValue(context, String.class), "demo");
//	}
}
