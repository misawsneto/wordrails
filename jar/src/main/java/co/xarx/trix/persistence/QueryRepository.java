package co.xarx.trix.persistence;

import co.xarx.trix.domain.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface QueryRepository<T extends Query> extends JpaRepository<T, Integer>, QueryDslPredicateExecutor<T> {
}