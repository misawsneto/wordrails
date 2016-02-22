package co.xarx.trix.persistence;

import co.xarx.trix.domain.query.statement.AbstractStatement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface ObjectStatementRepository extends JpaRepository<AbstractStatement, Integer>, QueryDslPredicateExecutor<AbstractStatement> {
}