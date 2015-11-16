package co.xarx.trix.domain.page;

import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.interfaces.Block;

public class PostBlock implements Block<Post> {

	public static final String TYPE = "block_post";

	public Post post;

	@Override
	public Post getObject() {
		return post;
	}

	@Override
	public String getType() {
		return TYPE;
	}
}
