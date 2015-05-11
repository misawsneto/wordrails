package com.wordrails.persistence;

import com.wordrails.business.Person;
import com.wordrails.business.Wordpress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

public interface PersonRepository extends JpaRepository<Person, Integer>, QueryDslPredicateExecutor<Person> {

	Person findByUsername(@Param("username") String username);
	
	Person findByEmail(@Param("email") String email);
    
    Person findByWordpress(@Param("wordpress") Wordpress wordpress);
	
}