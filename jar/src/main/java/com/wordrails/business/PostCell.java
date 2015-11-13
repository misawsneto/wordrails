package com.wordrails.business;

public class PostCell implements CellItem<Post> {

	public Post post;

	@Override
	public Post getObject() {
		return post;
	}
}
