package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.StationPerspective;
import com.wordrails.business.Taxonomy;

public interface StationPerspectiveRepository extends JpaRepository<StationPerspective, Integer>, QueryDslPredicateExecutor<StationPerspective> {
	@RestResource(exported=true, path="findStationPerspectivesByStation")
	List<StationPerspective> findByStationId(@Param("stationId") Integer stationId);

	@RestResource(exported=false)
	List<StationPerspective> findByTaxonomy(Taxonomy taxonomy);
}