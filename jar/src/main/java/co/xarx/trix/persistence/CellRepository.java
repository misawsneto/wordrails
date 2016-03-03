package co.xarx.trix.persistence;

import co.xarx.trix.domain.Cell;
import co.xarx.trix.domain.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface CellRepository extends DatabaseRepository<Cell> {

	@RestResource(exported=false)
	@Query("select cell from Cell cell " +
			"join cell.row row " +
			"where row.id = :rowId and cell.index >= :lowerLimit and cell.index < :upperLimit order by cell.index")
	List<Cell> findCellsPositioned(@Param("rowId") Integer rowId, @Param("lowerLimit") int lowerLimit, @Param("upperLimit") int upperLimit);

	@RestResource(exported=false)
	List<Cell> findByPost(Post post);
}