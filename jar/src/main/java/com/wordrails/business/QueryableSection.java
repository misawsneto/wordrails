package com.wordrails.business;

import com.wordrails.services.querydsl.QueryDslService;

public interface QueryableSection<T extends QueryDslService> {

	T getQueryDslService();
}
