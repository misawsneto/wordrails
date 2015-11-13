package com.wordrails.business.page;

import com.wordrails.services.querydsl.QueryDslService;

public interface QueryableSection<T extends QueryDslService> {

	T getQueryDslService();
}
