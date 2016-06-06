package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.CommentApi;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@NoArgsConstructor
public class CommentResource extends AbstractResource implements CommentApi {

	@Override
	public void findPostCommentsOrderByDate(Integer postId) throws IOException {
		forward();
	}
	@Override
	public void postComment() throws IOException {
		forward();
	}

	@Override
	public void putComment(Integer id) throws IOException {
		forward();
	}
}
