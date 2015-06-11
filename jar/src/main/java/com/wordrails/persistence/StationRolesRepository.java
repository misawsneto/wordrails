package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Person;
import com.wordrails.business.Station;
import com.wordrails.business.StationRole;

public interface StationRolesRepository extends JpaRepository<StationRole, Integer>, QueryDslPredicateExecutor<StationRole> {
	@RestResource(exported=false)
	StationRole findByStationAndPerson(Station station, Person person);
	
	@RestResource(exported=false)
	StationRole findByStationAndPersonId(Station station, Integer personId);

	@RestResource(exported=false)
	List<StationRole> findByStation(Station station);

	@RestResource(exported=false)
	List<StationRole> findByPersonIdAndNetworkId(@Param("personId") Integer personId, @Param("networkId") Integer networkId);
}