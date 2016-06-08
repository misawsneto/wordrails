package co.xarx.trix.elasticsearch.executor;

import co.xarx.trix.TestArtifactsFactory;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.util.ImmutablePage;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class ESPostExecutorTest {

	ESPostExecutor postExecutor;

	class PostSearchServiceStub implements PostSearchService {

		@Override
		public Pair searchPosts(String q, Integer personId, Integer page, Integer size, Collection<Integer> postIds) {
			return null;
		}

		@Override
		public Pair searchPosts(String q, Integer personId, Integer page, Integer size, boolean sortByDate) {
			return null;
		}

		@Override
		public ImmutablePage<Post> search(PostStatement params, Integer page, Integer size) {
			Post post1 = TestArtifactsFactory.createPost();
			post1.setTitle("post1");
			Post post2 = TestArtifactsFactory.createPost();
			post2.setTitle("post2");

			return new ImmutablePage<>(Lists.newArrayList(post1, post2), 0, 2);
		}

		@Override
		public ImmutablePage<PostData> searchData(PostStatement params, Integer page, Integer size) {
			return null;
		}
	}

	class PostSearchServiceNullStub implements PostSearchService {

		@Override
		public Pair searchPosts(String q, Integer personId, Integer page, Integer size, Collection<Integer> postIds) {
			return null;
		}

		@Override
		public Pair searchPosts(String q, Integer personId, Integer page, Integer size, boolean sortByDate) {
			return null;
		}

		@Override
		public ImmutablePage<Post> search(PostStatement params, Integer page, Integer size) {
			return null;
		}

		@Override
		public ImmutablePage<PostData> searchData(PostStatement params, Integer page, Integer size) {
			return null;
		}
	}

	@Before
	public void setUp() throws Exception {
		postExecutor = new ESPostExecutor(new PostSearchServiceStub());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testSizeZero() throws Exception {
		postExecutor.execute(new PostStatement(), 0, 10);
	}

	@Test
	public void testReturnPostData() throws Exception {
		ImmutablePage<PostData> postDatas = postExecutor.execute(new PostStatement(), 10, 0);
		Iterator<PostData> iterator = postDatas.iterator();

		assertEquals(postDatas.totalSize(), Integer.valueOf(2));
		assertEquals(postDatas.totalPages(), Integer.valueOf(1));
		assertEquals(iterator.next().getTitle(), "post1");
		assertEquals(iterator.next().getTitle(), "post2");
	}

	@Test
	public void testNullDataReturn() throws Exception {
		ESPostExecutor postExecutor = new ESPostExecutor(new PostSearchServiceNullStub());
		ImmutablePage<PostData> postDatas = postExecutor.execute(new PostStatement(), 10, 0);

		assertEquals(postDatas.totalSize(), Integer.valueOf(0));
		assertFalse(postDatas.iterator().hasNext());
	}
}