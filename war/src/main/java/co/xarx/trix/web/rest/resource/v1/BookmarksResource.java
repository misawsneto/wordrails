package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.services.post.PostService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.BookmarksApi;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class BookmarksResource extends AbstractResource implements BookmarksApi {

	private PostService postService;

	@Autowired
	public BookmarksResource(PostService postService) {
		this.postService = postService;
	}

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