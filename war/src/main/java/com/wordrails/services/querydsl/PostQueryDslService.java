package com.wordrails.services.querydsl;

import com.wordrails.domain.QPost;

public class PostQueryDslService extends AbstractQueryDslService {




	@Override
	public QPost getEntityPathBase() {
		return QPost.post1;
	}
}
