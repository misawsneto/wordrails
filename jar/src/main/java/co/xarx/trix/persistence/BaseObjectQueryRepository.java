package co.xarx.trix.persistence;

import co.xarx.trix.domain.query.AbstractObjectQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface BaseObjectQueryRepository extends JpaRepository<AbstractObjectQuery, Integer>, QueryDslPredicateExecutor<AbstractObjectQuery> {
}