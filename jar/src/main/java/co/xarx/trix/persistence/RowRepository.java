package co.xarx.trix.persistence;

import co.xarx.trix.domain.Row;
import co.xarx.trix.domain.Term;
import co.xarx.trix.domain.TermPerspective;
import org.hibernate.annotations.NamedNativeQuery;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface RowRepository extends DatabaseRepository<Row, Integer> {
	@RestResource(exported=false)
	List<Row> findByPerspective(@Param("perspective") TermPerspective termPerspective);
	@RestResource(exported=false)
	Row findFirstByPerspectiveOrderByIndexDesc(@Param("perspective") TermPerspective termPerspective);
	@RestResource(exported=false)
	Row findByPerspectiveAndTerm(@Param("perspective") TermPerspective termPerspective, @Param("term") Term term);
	@RestResource(exported=false)
	List<Row> findByTerm(Term term);

	@Modifying
	@RestResource(exported = false)
	@Query("DELETE FROM Row row WHERE row.featuringPerspective.id = :featuringPerspectiveId AND row.type = 'F' ")
	void deleteFeaturedRow(@Param("featuringPerspectiveId") Integer featuringPerspectiveId);
}