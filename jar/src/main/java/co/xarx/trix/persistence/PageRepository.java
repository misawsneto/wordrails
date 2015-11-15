package co.xarx.trix.persistence;

import co.xarx.trix.domain.page.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface PageRepository extends JpaRepository<Page, Integer>, QueryDslPredicateExecutor<Page> {
}