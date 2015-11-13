package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.StationPerspective;
import com.wordrails.business.TermPerspective;

public interface TermPerspectiveRepository extends JpaRepository<TermPerspective, Integer>, QueryDslPredicateExecutor<TermPerspective> {
	@RestResource(exported=false)
	List<TermPerspective> findByPerspective(@Param("perspective") StationPerspective stationPerspective); 
	@RestResource(exported=false)
	TermPerspective findRootTermByStationPerspective(@Param("stationPerspectiveId") Integer stationPerspectiveId);
	@RestResource(exported=false)
	TermPerspective findPerspectiveAndTerm(@Param("stationPerspectiveId") Integer stationPerspectiveId, @Param("termId") Integer termId);
	@RestResource(exported=false)
	TermPerspective findPerspectiveAndTermNull(@Param("stationPerspectiveId") Integer stationPerspectiveId);
}