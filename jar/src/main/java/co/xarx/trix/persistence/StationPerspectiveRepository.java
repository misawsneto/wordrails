package co.xarx.trix.persistence;

import co.xarx.trix.domain.StationPerspective;
import co.xarx.trix.domain.Taxonomy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface StationPerspectiveRepository extends JpaRepository<StationPerspective, Integer> {

	@Query("select stationPerspective from StationPerspective stationPerspective where stationPerspective.station.id = :stationId")
	@RestResource(exported=true, path="findStationPerspectivesByStation")
	List<StationPerspective> findByStationId(@Param("stationId") Integer stationId);

	@RestResource(exported=false)
	List<StationPerspective> findByTaxonomy(Taxonomy taxonomy);
}