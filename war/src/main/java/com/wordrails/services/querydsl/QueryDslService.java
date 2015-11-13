package com.wordrails.services.querydsl;

import com.mysema.query.types.path.EntityPathBase;

public interface QueryDslService<T extends EntityPathBase> {

	T getEntityPathBase();
}
