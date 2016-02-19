package co.xarx.trix.persistence;

import co.xarx.trix.domain.page.AbstractSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface BaseSectionRepository extends JpaRepository<AbstractSection, Integer>, QueryDslPredicateExecutor<AbstractSection> {
}