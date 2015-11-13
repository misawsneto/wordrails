package co.xarx.trix.persistence;

import java.util.List;

import co.xarx.trix.domain.Row;
import co.xarx.trix.domain.Term;
import co.xarx.trix.domain.TermPerspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface RowRepository extends JpaRepository<Row, Integer>, QueryDslPredicateExecutor<Row> {
	@RestResource(exported=false)
	List<Row> findByPerspective(@Param("perspective") TermPerspective termPerspective);
	@RestResource(exported=false)
	Row findFirstByPerspectiveOrderByIndexDesc(@Param("perspective") TermPerspective termPerspective);
	@RestResource(exported=false)
	Row findByPerspectiveAndTerm(@Param("perspective") TermPerspective termPerspective, @Param("term") Term term);
	@RestResource(exported=false)
	List<Row> findByTerm(Term term);
}