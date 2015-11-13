package co.xarx.trix.persistence;

import co.xarx.trix.domain.StationPerspective;
import co.xarx.trix.domain.TermPerspective;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface TermPerspectiveRepository extends JpaRepository<TermPerspective, Integer>, QueryDslPredicateExecutor<TermPerspective> {

	@RestResource(exported=false)
	List<TermPerspective> findByPerspective(@Param("perspective") StationPerspective stationPerspective);

	@RestResource(exported=false)
	@Query("select termPerspective " +
			"from TermPerspective termPerspective join termPerspective.term term join termPerspective.perspective " +
			"perspective " +
			"where term.id = :termId and perspective.id = :stationPerspectiveId")
	TermPerspective findPerspectiveAndTerm(@Param("stationPerspectiveId") Integer stationPerspectiveId, @Param("termId") Integer termId);


	@RestResource(exported=false)
	@Query("select termPerspective " +
			"from TermPerspective termPerspective join termPerspective.perspective perspective " +
			"where termPerspective.term is null and perspective.id = :stationPerspectiveId")
	TermPerspective findPerspectiveAndTermNull(@Param("stationPerspectiveId") Integer stationPerspectiveId);
}