package com.wordrails.persistence;

import com.wordrails.business.Person;
import com.wordrails.business.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

public interface PersonRepository extends JpaRepository<Person, Integer>, QueryDslPredicateExecutor<Person> {

	Set<Person> findByUsername(@Param("username") String username);

	@RestResource(exported = false)
	Person findByUser(@Param("user") User user);

	Person findByEmail(@Param("email") String email);

	@RestResource(exported = false)
	Person findByWordpressId(@Param("wordpressId") Integer wordpressId);
}