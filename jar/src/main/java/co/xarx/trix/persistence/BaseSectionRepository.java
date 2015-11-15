package co.xarx.trix.persistence;

import co.xarx.trix.domain.page.BaseSection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface BaseSectionRepository extends JpaRepository<BaseSection, Integer>, QueryDslPredicateExecutor<BaseSection> {
}