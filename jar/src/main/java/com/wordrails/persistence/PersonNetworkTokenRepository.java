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
	public List<PersonNetworkToken> findByPersonAndNetwork(@Param("person") Person person, @Param("network") Network network);
	
	@RestResource(exported=false)
	public List<PersonNetworkToken> findByNetwork(@Param("network") Network network);

//	 SELECT * FROM 
//	 personnetworkid  join person p on .person_id = p.id
//	 where p.id in 
//	 (select psr.person_id 
//	 from person_station_role psr 
//	 where psr.station_id = :stationId) OR 
//	 (.person_id = 1 and .network_id in 
//	 (select sn.networks_id from station_network sn 
//	 join network n on sn.networks_id = n.id 
//	 join station s on sn.stations_id = s.id 
//	 where s.visibility = "UNRESTRICTED"))

	@RestResource(exported=false)
	@Query("SELECT token FROM PersonNetworkToken token join token.person p where p in ( select psr.person from StationRole psr where psr.station.id = :stationId AND psr.person.id != 1 ) OR ( (token.person is null OR token.person.id = 1) and token.network in ( select network from Network network join network.stations s where s.id = :stationId AND s.visibility = 'UNRESTRICTED') )")
	public List<PersonNetworkToken> findTokenByStationId(@Param("stationId") Integer stationId);

	@RestResource(exported = false)
	@Query(nativeQuery=true, value="select id from network where id = (select networks_id from station_network where stations_id = ?)")
	public Integer findNetworkIdByStationId(Integer stationId);

	@RestResource(exported=false)
	public void deleteByToken(String token);

	@RestResource(exported=false)
	public PersonNetworkToken findOneByToken(String token);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);
}