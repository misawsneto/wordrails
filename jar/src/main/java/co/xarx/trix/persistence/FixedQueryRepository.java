package co.xarx.trix.persistence;

import co.xarx.trix.domain.query.FixedQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface FixedQueryRepository  extends JpaRepository<FixedQuery, Integer>, QueryDslPredicateExecutor<FixedQuery> {
}