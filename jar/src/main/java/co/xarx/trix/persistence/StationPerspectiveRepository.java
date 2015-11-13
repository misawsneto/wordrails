package co.xarx.trix.persistence;

import java.util.List;

import co.xarx.trix.domain.StationPerspective;
import co.xarx.trix.domain.Taxonomy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface StationPerspectiveRepository extends JpaRepository<StationPerspective, Integer>, QueryDslPredicateExecutor<StationPerspective> {
	@RestResource(exported=true, path="findStationPerspectivesByStation")
	List<StationPerspective> findByStationId(@Param("stationId") Integer stationId);

	@RestResource(exported=false)
	List<StationPerspective> findByTaxonomy(Taxonomy taxonomy);
}