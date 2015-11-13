package co.xarx.trix.persistence;

import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.Taxonomy;
import co.xarx.trix.domain.Wordpress;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface TaxonomyRepository extends JpaRepository<Taxonomy, Integer>, QueryDslPredicateExecutor<Taxonomy> {
	Taxonomy findByTypeAndName(@Param("type") String type, @Param("name") String name);

	List<Taxonomy> findByStationId(@Param("stationId") Integer stationId);

	@Query("select taxonomy from Taxonomy taxonomy where taxonomy.type = 'S' and taxonomy.owningStation.id = :stationId")
	List<Taxonomy> findStationTaxonomy(@Param("stationId") Integer stationId);

	@Query("select taxonomy from Taxonomy taxonomy where taxonomy.type = 'T' and taxonomy.owningStation.id = :stationId")
	List<Taxonomy> findStationTags(@Param("stationId") Integer stationId);

	@Query("select taxonomy from Taxonomy taxonomy where taxonomy.type = 'N' and taxonomy.owningNetwork.id = :networkId")
	List<Taxonomy> findNetworkCategories(@Param("networkId") Integer networkId);

	@RestResource(exported=false)
	List<Taxonomy> findNetworkOrStationTaxonomiesByNetworkIdExcludeType(@Param("networkId") Integer networkId, @Param("type") String type);

	@RestResource(exported=false)
	List<Taxonomy> findByStationsIds(@Param("stationsIds") List<Integer> stationsIds);

	@RestResource(exported=false)
	List<Taxonomy> findByTermsIds(@Param("termsIds") List<Integer> termsIds);
	
	@RestResource(exported=false)
	Taxonomy findByTermsId(Integer termId);

	@RestResource(exported=false)
	Taxonomy findTypeTByWordpress(@Param("wordpress") Wordpress wordpress); //taxonomy of type T

	@RestResource(exported=false)
	Taxonomy findByWordpress(@Param("wordpress") Wordpress wordpress); //taxonomy of all types except T or A

	@RestResource(exported=false)
	Taxonomy findTypeTByStation(@Param("station") Station station); //taxonomy of type T

	@RestResource(exported=false)
	Taxonomy findByStation(@Param("station") Station station); //taxonomy of all types except T or A
	
	@RestResource(exported=false)
	Taxonomy findAuthorTaxonomyByStationId(@Param("station") Station station, @Param("taxonomyType") String taxonomyType);
	
	@RestResource(exported=false)
	@Modifying
	@Query(nativeQuery=true, value="DELETE FROM network_taxonomy WHERE taxonomies_id = ?")
	void deleteTaxonomyNetworks(Integer taxonomyId);
}