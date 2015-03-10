package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Network;
import com.wordrails.business.NetworkRole;
import com.wordrails.business.Person;

public interface NetworkRolesRepository extends JpaRepository<NetworkRole, Integer>, QueryDslPredicateExecutor<NetworkRole> {
	@RestResource(exported=false)
	NetworkRole findByNetworkAndPerson(Network network, Person person);

	@RestResource(exported=false)
	NetworkRole findByNetworkIdAndPersonId(@Param("networkId") Integer idNetwork, @Param("personId") Integer personId);
	
	@RestResource(exported=false)
	List<NetworkRole> findByPersonId(@Param("personId") Integer personId);
}