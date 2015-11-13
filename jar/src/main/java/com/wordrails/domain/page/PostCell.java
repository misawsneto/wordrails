package com.wordrails.domain.page;

import com.wordrails.domain.Post;

public class PostCell implements CellItem<Post> {

	public Post post;

	@Override
	public Post getObject() {
		return post;
	}
}
