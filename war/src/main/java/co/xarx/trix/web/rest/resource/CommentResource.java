package co.xarx.trix.web.rest.resource;

import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.CommentApi;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CommentResource extends AbstractResource implements CommentApi {

	@Override
	public void findPostCommentsOrderByDate(Integer postId) throws IOException {
		forward();
	}
	@Override
	public void postComment() throws IOException {
		forward();
	}
}
