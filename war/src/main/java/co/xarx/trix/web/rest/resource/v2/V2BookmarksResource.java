package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.services.post.PostService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2BookmarksApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class V2BookmarksResource extends AbstractResource implements V2BookmarksApi {
	@Autowired
	private PostService postService;

	@Override
	public ContentResponse<List<PostView>> searchBookmarks() {

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postService.searchBookmarks();

		return response;

	}

	@Override
	public BooleanResponse toggleBookmark(Integer postId) {
		BooleanResponse br = new BooleanResponse();
		br.response = postService.toggleBookmark(postId, request);
		return br;
	}
}
