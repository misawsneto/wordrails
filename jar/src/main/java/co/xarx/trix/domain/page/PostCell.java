package co.xarx.trix.domain.page;

import co.xarx.trix.domain.Post;

public class PostCell implements CellItem<Post> {

	public Post post;

	@Override
	public Post getObject() {
		return post;
	}
}
