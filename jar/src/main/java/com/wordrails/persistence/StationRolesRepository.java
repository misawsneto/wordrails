package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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
	StationRole findByStationIdAndPersonId(Integer stationId, Integer personId);
	
	@RestResource(exported=false)
	List<StationRole> findByStationAndPersonId(Station station, Integer personId, Pageable pageable);

	@RestResource(exported=false)
	List<StationRole> findByStation(Station station);

	@RestResource(exported=false)
	List<StationRole> findByPersonIdAndNetworkId(@Param("personId") Integer personId, @Param("networkId") Integer networkId);
	
	@Query("SELECT str FROM StationRole str WHERE str.station.id in (:stationIds) order by str.person.id desc")
	List<StationRole> findByStationIds(@Param("stationIds") List<Integer> stationIds, Pageable pageable);
	
	@Query("SELECT str FROM StationRole str WHERE str.station.id in (:stationIds) AND (str.person.name = :nameOrUseranmeOrEmail OR str.person.username = :nameOrUseranmeOrEmail OR str.person.email = :nameOrUseranmeOrEmail) order by str.person.id desc")
	List<StationRole> findByStationIdsAndNameOrUseranmeOrEmail(@Param("stationIds") List<Integer> stationIds, @Param("nameOrUseranmeOrEmail") String nameOrUseranmeOrEmail, Pageable pageable);

	@RestResource(exported=false)
	List<StationRole> findByPerson(Person personLogged);

	@RestResource(exported=false)
	@Query("SELECT str FROM StationRole str WHERE str.station.id in (:stationIds) AND str.person.id = :personId")
	List<StationRole> findByPersonAndStationIds(@Param("personId") Integer personId, @Param("stationIds") List<Integer> stationIds);

	@RestResource(exported=false)
	@Modifying
	@Query("delete from StationRole str where str.id in (:stationRolesIds)")
	void deleteByIds(@Param("stationRolesIds") List<Integer> stationRolesIds);
}