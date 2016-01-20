package co.xarx.trix.persistence;

import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.PersonNetworkToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface PersonNetworkTokenRepository extends JpaRepository<PersonNetworkToken, Integer>, QueryDslPredicateExecutor<PersonNetworkToken> {
	@RestResource(exported=false)
	List<PersonNetworkToken> findByPersonAndNetwork(@Param("person") Person person, @Param("network") Network network);
	
	@RestResource(exported=false)
	List<PersonNetworkToken> findByNetwork(@Param("network") Network network);

	@RestResource(exported=false)
	@Query("SELECT token from PersonNetworkToken token where token.network.id = :networkId")
	List<PersonNetworkToken> findByNetworkId(@Param("networkId") Integer networkId);

	@RestResource(exported=false)
	@Query("SELECT token FROM PersonNetworkToken token where token.person in (select psr.person from StationRole psr where psr.station.id = :stationId)")
	List<PersonNetworkToken> findTokenByStationId(@Param("stationId") Integer stationId);

	@RestResource(exported=false)
	void deleteByToken(String token);

	@RestResource(exported=false)
	PersonNetworkToken findOneByToken(String token);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);
}