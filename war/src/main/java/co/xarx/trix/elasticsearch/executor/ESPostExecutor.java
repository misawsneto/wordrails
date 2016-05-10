package co.xarx.trix.elasticsearch.executor;

import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.Executor;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.util.RestUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component("post_executor")
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class ESPostExecutor implements Executor<PostData, PostStatement> {

	private ModelMapper mapper;
	private PostSearchService searchService;

	@Autowired
	public ESPostExecutor(ModelMapper mapper, PostSearchService searchService) {
		this.mapper = mapper;
		this.searchService = searchService;
	}

	@Override
	public Page<PostData> execute(PostStatement params, Integer size, Integer from) {
		Assert.notNull(params, "PostStatement must not be null");
		Assert.notNull(size, "size must not be null");
		Assert.isTrue(size > 0, "size must be higher than zero");
		Assert.notNull(from, "from must not be null");

		int page = from / size;
		List<Post> posts = searchService.search(params, page, size);
		List<PostData> data = getPostDatas(posts);
//		List<PostData> data = searchService.searchData(params, page, size);

		Pageable pageable = RestUtil.getPageable(page, size, params.getOrders());

		int contentSize = data != null ? data.size() : 0;
		return new PageImpl(data, pageable, contentSize);
	}

	private List<PostData> getPostDatas(List<Post> posts) {
		if(posts == null || posts.isEmpty())
			return new ArrayList<>();

		return posts.stream()
				.map(post -> mapper.map(post, PostData.class))
				.collect(Collectors.toList());
	}
}