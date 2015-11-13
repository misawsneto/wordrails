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
import com.wordrails.business.PersonNetworkRegId;

public interface PersonNetworkRegIdRepository extends JpaRepository<PersonNetworkRegId, Integer>, QueryDslPredicateExecutor<PersonNetworkRegId> {
	@RestResource(exported=false)
	public List<PersonNetworkRegId> findByPersonAndNetwork(@Param("person") Person person, @Param("network") Network network);
	
	@RestResource(exported=false)
	public List<PersonNetworkRegId> findByNetwork(@Param("network") Network network);

	@RestResource(exported = false)
	@Query("SELECT regId from PersonNetworkRegId regId where regId.network.id = :networkId")
	public List<PersonNetworkRegId> findRegIdByNetworkId(@Param("networkId") Integer networkId);

//	 SELECT * FROM 
//	 personnetworkregid reg join person p on reg.person_id = p.id
//	 where p.id in 
//	 (select psr.person_id 
//	 from person_station_role psr 
//	 where psr.station_id = :stationId) OR 
//	 (reg.person_id = 1 and reg.network_id in 
//	 (select sn.networks_id from station_network sn 
//	 join network n on sn.networks_id = n.id 
//	 join station s on sn.stations_id = s.id 
//	 where s.visibility = "UNRESTRICTED"))

//	@RestResource(exported=false)
//	@Query("SELECT reg FROM PersonNetworkRegId reg join reg.person p where p in ( select psr.person from StationRole psr where psr.station.id = :stationId AND psr.person.id != 1 ) OR ( (reg.person is null OR reg.person.id = 1) and reg.network in ( select network from Network network join network.stations s where s.id = :stationId AND s.visibility = 'UNRESTRICTED') ) order by reg.id desc")
//	public List<PersonNetworkRegId> findRegIdByStationId(@Param("stationId") Integer stationId);

	@RestResource(exported=false)
	@Query("SELECT token FROM PersonNetworkRegId token where token.person in (select psr.person from StationRole psr where psr.station.id = :stationId)")
	List<PersonNetworkRegId> findRegIdByStationId(@Param("stationId") Integer stationId);

	@RestResource(exported=false)
	public void deleteByRegId(String canonicalRegistrationId);

	@RestResource(exported=false)
	public PersonNetworkRegId findOneByRegId(String regId);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);
}