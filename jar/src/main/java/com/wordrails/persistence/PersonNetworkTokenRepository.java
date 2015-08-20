package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Network;
import com.wordrails.business.Person;
import com.wordrails.business.PersonNetworkToken;

public interface PersonNetworkTokenRepository extends JpaRepository<PersonNetworkToken, Integer>, QueryDslPredicateExecutor<PersonNetworkToken> {
	@RestResource(exported=false)
	List<PersonNetworkToken> findByPersonAndNetwork(@Param("person") Person person, @Param("network") Network network);
	
	@RestResource(exported=false)
	List<PersonNetworkToken> findByNetwork(@Param("network") Network network);

	@RestResource(exported=false)
	@Query("SELECT token FROM PersonNetworkToken token where token.person in (select psr.person from StationRole psr where psr.station.id = :stationId)")
	List<PersonNetworkToken> findByStationId(@Param("stationId") Integer stationId);

	@RestResource(exported=false)
	void deleteByToken(String token);

	@RestResource(exported=false)
	PersonNetworkToken findOneByToken(String token);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);
}