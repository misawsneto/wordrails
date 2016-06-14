package co.xarx.trix.elasticsearch.executor;

import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.page.query.Executor;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.util.ImmutablePage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

@Component("post_executor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ESPostExecutor implements Executor<PostData, PostStatement> {

	private PostSearchService searchService;

	@Autowired
	public ESPostExecutor(PostSearchService searchService) {
		this.searchService = searchService;
	}

	@Override
	public ImmutablePage<PostData> execute(PostStatement params, Integer size, Integer from) {
		Assert.notNull(params, "PostStatement must not be null");
		Assert.notNull(size, "size must not be null");
		Assert.isTrue(size > 0, "size must be higher than zero");
		Assert.notNull(from, "from must not be null");

		int page = from / size;
//		List<Post> posts = searchService.search(params, page, size);
//		List<PostData> data = getPostDatas(posts);

		ImmutablePage<PostData> postDatas = searchService.searchData(params, page, size);
		if(postDatas == null)
			postDatas = new ImmutablePage<>();
		return postDatas;
	}

//	private List<PostData> getPostDatas(List<Post> posts) {
//		if(posts == null || posts.isEmpty())
//			return new ArrayList<>();
//
//		return posts.stream()
//				.map(post -> mapper.map(post, PostData.class))
//				.collect(Collectors.toList());
//	}
}