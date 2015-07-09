package com.wordrails.persistence;

import java.util.List;

import com.wordrails.business.Person;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PersonRepository extends JpaRepository<Person, Integer>, QueryDslPredicateExecutor<Person> {

	Person findByUsername(@Param("username") String username);
	
	Person findByEmail(@Param("email") String email);
    
	@RestResource(exported=false)
    Person findByWordpressId(@Param("wordpressId") Integer wordpressId);
}