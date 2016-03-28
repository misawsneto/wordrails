package co.xarx.trix.persistence;

import co.xarx.trix.domain.StationPerspective;
import co.xarx.trix.domain.TermPerspective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface TermPerspectiveRepository extends DatabaseRepository<TermPerspective, Integer> {

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
			"from TermPerspective termPerspective " +
			"where termPerspective.term is null and termPerspective.perspective.id = :stationPerspectiveId")
	TermPerspective findPerspectiveAndTermNull(@Param("stationPerspectiveId") Integer stationPerspectiveId);
}