package com.wordrails.persistence;

import com.wordrails.business.Network;
import com.wordrails.business.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Integer>, QueryDslPredicateExecutor<Person> {

	Set<Person> findByUsername(@Param("username") String username);

	@RestResource(exported = false)
	Person findByUsernameAndNetwork(@Param("username") String username, @Param("network") Network network);

	Person findByEmail(@Param("email") String email);

	@RestResource(exported = false)
	Person findByWordpressId(@Param("wordpressId") Integer wordpressId);
}