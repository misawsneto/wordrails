package co.xarx.trix.persistence;

import co.xarx.trix.domain.query.ElasticSearchQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface ElasticSearchQueryRepository extends JpaRepository<ElasticSearchQuery, Integer>, QueryDslPredicateExecutor<ElasticSearchQuery> {
}