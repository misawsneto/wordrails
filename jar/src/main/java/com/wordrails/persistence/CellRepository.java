package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.domain.Cell;
import com.wordrails.domain.Post;
import com.wordrails.domain.Row;

public interface CellRepository extends JpaRepository<Cell, Integer>, QueryDslPredicateExecutor<Cell> {
	@RestResource(exported=false)
	List<Cell> findByRow(@Param("row") Row row);
	@RestResource(exported=false)
	List<Cell> findCellsPositioned(@Param("rowId") Integer rowId, @Param("lowerLimit") int lowerLimit, @Param("upperLimit") int upperLimit);
	@RestResource(exported=false)
	List<Cell> findByPost(Post post);
}