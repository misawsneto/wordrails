package com.wordrails.domain.page;

import com.wordrails.domain.query.Query;

public interface QueryableSection<T extends Query> {

	T getQuery();
}
