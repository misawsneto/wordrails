package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.services.post.PostService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2RecommendApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class V2RecommendsResource extends AbstractResource implements V2RecommendApi {
	private PostService postService;

	@Autowired
	public V2RecommendsResource(PostService postService) {
		this.postService = postService;
	}

	@Override
	public ContentResponse<List<PostView>> searchRecommends(String q, Integer page, Integer size) {
		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postService.searchRecommends(q, page, size);

		return response;
	}

	@Override
	public BooleanResponse toggleRecommend(Integer postId) {
		BooleanResponse br = new BooleanResponse();
		br.response = postService.toggleRecommend(postId, request);
		return br;
	}
}
