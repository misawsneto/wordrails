package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Station;
import com.wordrails.business.Taxonomy;

public interface TaxonomyRepository extends JpaRepository<Taxonomy, Integer>, QueryDslPredicateExecutor<Taxonomy> {
	Taxonomy findByTypeAndName(@Param("type") String type, @Param("name") String name);

	List<Taxonomy> findByStationId(@Param("stationId") Integer stationId);

	@RestResource(exported=false)
	List<Taxonomy> findNetworkOrStationTaxonomiesByNetworkIdExcludeType(@Param("networkId") Integer networkId, @Param("type") String type);

	@RestResource(exported=false)
	List<Taxonomy> findByStationsIds(@Param("stationsIds") List<Integer> stationsIds);

	@RestResource(exported=false)
	List<Taxonomy> findByTermsIds(@Param("termsIds") List<Integer> termsIds);
	
	@RestResource(exported=false)
	Taxonomy findByTermsId(Integer termId);
	
	@RestResource(exported=false)
	Taxonomy findAuthorTaxonomyByStationId(@Param("station") Station station, @Param("taxonomyType") String taxonomyType);
	
	@RestResource(exported=false)
	@Modifying
	@Query(nativeQuery=true, value="DELETE FROM network_taxonomy WHERE taxonomies_id = ?")
	void deleteTaxonomyNetworks(Integer taxonomyId);
}