package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.PersonNetworkRegId;

public interface PersonNetworkRegIdRepository extends JpaRepository<PersonNetworkRegId, Integer>, QueryDslPredicateExecutor<PersonNetworkRegId> {
	@RestResource(exported=false)
	public List<PersonNetworkRegId> findByPersonAndNetwork(@Param("person") Person person, @Param("network") Network network);
	
	@RestResource(exported=false)
	public List<PersonNetworkRegId> findByNetwork(@Param("network") Network network);

	@Query("select pnr from PersonNetworkRegId pnr join pnr.person person where person in (select pst.id from StationRole pst where pst.station.id = :stationId )")
	@RestResource(exported=false)
	public List<PersonNetworkRegId> findRegIdByStationId(@Param("stationId") Integer stationId);
}