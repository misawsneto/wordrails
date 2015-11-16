package co.xarx.trix.persistence;

import co.xarx.trix.domain.query.BaseQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface BaseQueryRepository extends JpaRepository<BaseQuery, Integer>, QueryDslPredicateExecutor<BaseQuery> {
}