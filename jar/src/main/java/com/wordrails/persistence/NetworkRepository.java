package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Network;
import com.wordrails.business.Station;
import com.wordrails.business.Taxonomy;

public interface NetworkRepository extends JpaRepository<Network, Integer>, QueryDslPredicateExecutor<Network> {
	@RestResource(exported=false)
	List<Network> findByStations(Station station);

	@RestResource(exported=false)
	List<Integer> findIdsByStation(@Param("stationId") Integer stationId);
	
	@RestResource(exported=false)
	List<Network> belongsToNetworks(@Param("personId") Integer personId, @Param("networksId") List<Integer> networksId);

	@RestResource(exported=true)
	List<Network> findBySubdomain(@Param("subdomain") String subdomain);
	
	@RestResource(exported=false)
	Network findNetworkBySubdomain(@Param("subdomain") String subdomain);
	
	@RestResource(exported=true)
	Network findOneBySubdomain(@Param("subdomain") String subdomain);
	
	@RestResource(exported=false)
	Network findByDomain(String domain);
}