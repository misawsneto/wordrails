package com.wordrails.business.page;

import com.wordrails.business.Post;

public class PostCell implements CellItem<Post> {

	public Post post;

	@Override
	public Post getObject() {
		return post;
	}
}
