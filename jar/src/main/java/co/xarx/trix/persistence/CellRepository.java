package co.xarx.trix.persistence;

import java.util.List;

import co.xarx.trix.domain.Cell;
import co.xarx.trix.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import co.xarx.trix.domain.Row;

public interface CellRepository extends JpaRepository<Cell, Integer>, QueryDslPredicateExecutor<Cell> {
	@RestResource(exported=false)
	List<Cell> findByRow(@Param("row") Row row);
	@RestResource(exported=false)
	List<Cell> findCellsPositioned(@Param("rowId") Integer rowId, @Param("lowerLimit") int lowerLimit, @Param("upperLimit") int upperLimit);
	@RestResource(exported=false)
	List<Cell> findByPost(Post post);
}