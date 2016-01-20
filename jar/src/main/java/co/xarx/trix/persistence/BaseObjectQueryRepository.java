package co.xarx.trix.persistence;

import co.xarx.trix.domain.query.BaseObjectQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface BaseObjectQueryRepository extends JpaRepository<BaseObjectQuery, Integer>, QueryDslPredicateExecutor<BaseObjectQuery> {
}