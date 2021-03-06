package co.xarx.trix.persistence;

import co.xarx.trix.domain.page.query.PageableQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface PageableQueryRepository extends JpaRepository<PageableQuery, Integer>,
		QueryDslPredicateExecutor<PageableQuery> {
}