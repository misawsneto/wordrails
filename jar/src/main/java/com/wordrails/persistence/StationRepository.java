package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Station;

public interface StationRepository extends JpaRepository<Station, Integer>, QueryDslPredicateExecutor<Station> {
	List<Station> findByName(@Param("name") String name);

	List<Station> findByPersonIdAndNetworkId(@Param("personId") Integer personId, @Param("networkId") Integer networkId);
	
	@RestResource(exported=false)
	Station belongsToStation(@Param("personId") Integer personId, @Param("stationId") Integer stationId);
	
	@RestResource(exported=false)
	List<Station> belongsToStations(@Param("personId") Integer personId, @Param("stationsId") List<Integer> stationsId);
	
	@RestResource(exported=false)
	@Modifying
	@Query(nativeQuery=true, value="DELETE FROM station_network WHERE stations_id = ?")
	void deleteStationNetwork(Integer stationId);

}