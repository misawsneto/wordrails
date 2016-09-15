package co.xarx.trix.persistence;

import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.Taxonomy;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = true)
public interface TaxonomyRepository extends JpaRepository<Taxonomy, Integer>, QueryDslPredicateExecutor<Taxonomy> {

	Taxonomy findByTypeAndName(@Param("type") String type, @Param("name") String name);

	@Query("select taxonomy from Taxonomy taxonomy where taxonomy.type = 'S' and taxonomy.owningStation.id = :stationId")
	List<Taxonomy> findStationTaxonomy(@Param("stationId") Integer stationId);

//	@Query("select taxonomy from Taxonomy taxonomy where taxonomy.type = 'T' and taxonomy.owningStation.id = :stationId")
//	List<Taxonomy> findStationTags(@Param("stationId") Integer stationId);

//	@Query("select taxonomy from Taxonomy taxonomy where taxonomy.type = 'N' and taxonomy.owningNetwork.id = :networkId")
//	List<Taxonomy> findNetworkCategories(@Param("networkId") Integer networkId);

//	@Query("select t from Taxonomy t where t.id IN " +
//			"(select taxonomy.id from Taxonomy taxonomy join taxonomy.owningStation station where " +
//			"station.network.id = :networkId) " +
//			"OR t.id IN " +
//			"(select taxonomy.id from Taxonomy taxonomy join taxonomy.owningNetwork network where network.id = :networkId)"
//	)
//	List<Taxonomy> findNetworkOrStationTaxonomies(@Param("networkId") Integer networkId);

	@Query("select t from Taxonomy t join t.owningStation station where station.id IN (:stationsIds)")
	@RestResource(exported=false)
	List<Taxonomy> findByStationsIds(@Param("stationsIds") List<Integer> stationsIds);

	@RestResource(exported=false)
	@Modifying
	@Query(nativeQuery=true, value="DELETE FROM network_taxonomy WHERE taxonomies_id = ?")
	void deleteTaxonomyNetworks(Integer taxonomyId);

	@RestResource(exported=false)
	public  Taxonomy findByOwningStationId(Integer id);
}