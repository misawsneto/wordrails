package com.wordrails.business.page;

import com.wordrails.business.query.Query;

public interface QueryableSection<T extends Query> {

	T getQuery();
}
