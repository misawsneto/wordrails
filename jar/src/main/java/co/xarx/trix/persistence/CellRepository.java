package co.xarx.trix.persistence;

import co.xarx.trix.domain.Cell;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Row;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface CellRepository extends JpaRepository<Cell, Integer>, QueryDslPredicateExecutor<Cell> {

	@RestResource(exported=false)
	List<Cell> findByRow(@Param("row") Row row);

	@RestResource(exported=false)
	@Query("select cell from Cell cell " +
			"join cell.row row " +
			"where row.id = :rowId and cell.index >= :lowerLimit and cell.index < :upperLimit order by cell.index")
	List<Cell> findCellsPositioned(@Param("rowId") Integer rowId, @Param("lowerLimit") int lowerLimit, @Param("upperLimit") int upperLimit);

	@RestResource(exported=false)
	List<Cell> findByPost(Post post);
}