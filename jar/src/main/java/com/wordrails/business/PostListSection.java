package com.wordrails.business;

import com.wordrails.services.querydsl.PostQueryDslService;

import java.util.List;

public class PostListSection implements ListSection<PostCell>, QueryableSection<PostQueryDslService> {

	@Override
	public List<PostCell> getCells() {
		return null;
	}

	@Override
	public PostQueryDslService getQueryDslService() {
		return null;
	}
}
