package co.xarx.trix.elasticsearch.executor;

import co.xarx.trix.TestArtifactsFactory;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.elasticsearch.mapper.PostViewMap;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.web.rest.map.*;
import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ESPostExecutorTest {

	ESPostExecutor postExecutor;
	private ModelMapper dataMapper;

	class PostSearchServiceStub implements PostSearchService {

		@Override
		public List<Post> search(List<Integer> ids, Integer page, Integer size) {
			Post post1 = TestArtifactsFactory.createPost();
			post1.setTitle("post1");
			Post post2 = TestArtifactsFactory.createPost();
			post2.setTitle("post2");

			return Lists.newArrayList(post1, post2);
		}

		@Override
		public List<Integer> searchIds(PostStatement params) {
			return Lists.newArrayList(1, 2);
		}
	}

	class PostSearchServiceNullStub implements PostSearchService {

		@Override
		public List<Post> search(List<Integer> ids, Integer page, Integer size) {
			return null;
		}

		@Override
		public List<Integer> searchIds(PostStatement params) {
			return null;
		}
	}

	@Before
	public void setUp() throws Exception {
		dataMapper = new ModelMapper();
		dataMapper.addMappings(new PictureDataMap());
		dataMapper.addMappings(new ImageDataMap());
		dataMapper.addMappings(new PersonDataMap());
		dataMapper.addMappings(new PostDataMap());
		dataMapper.addMappings(new PageDataMap());
		dataMapper.addMappings(new PostViewMap());

		postExecutor = new ESPostExecutor(dataMapper, new PostSearchServiceStub());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSizeZero() throws Exception {
		postExecutor.execute(new PostStatement(), 0, 10);
	}

	@Test
	public void testReturnPostData() throws Exception {
		Page<PostData> postDatas = postExecutor.execute(new PostStatement(), 10, 0);
		Iterator<PostData> iterator = postDatas.iterator();

		assertEquals(postDatas.getTotalElements(), 2);
		assertEquals(postDatas.getTotalPages(), 1);
		assertEquals(iterator.next().getTitle(), "post1");
		assertEquals(iterator.next().getTitle(), "post2");
	}

	@Test
	public void testNullDataReturn() throws Exception {
		ESPostExecutor postExecutor = new ESPostExecutor(dataMapper, new PostSearchServiceNullStub());
		Page<PostData> postDatas = postExecutor.execute(new PostStatement(), 10, 0);

		assertEquals(postDatas.getTotalElements(), 0);
		assertFalse(postDatas.iterator().hasNext());
	}
}