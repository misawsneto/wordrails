package co.xarx.trix.persistence;

import co.xarx.trix.domain.Network;
import co.xarx.trix.domain.NetworkRole;
import co.xarx.trix.domain.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface NetworkRolesRepository extends JpaRepository<NetworkRole, Integer>, QueryDslPredicateExecutor<NetworkRole> {
	@RestResource(exported=false)
	NetworkRole findByNetworkAndPerson(Network network, Person person);

	@RestResource(exported=false)
	NetworkRole findByNetworkIdAndPersonId(@Param("networkId") Integer idNetwork, @Param("personId") Integer personId);
	
	@RestResource(exported=false)
	NetworkRole findByPerson(@Param("person") Person person);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);
}