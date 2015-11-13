package com.wordrails.persistence;

import com.wordrails.business.Station;
import com.wordrails.business.Wordpress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface WordpressRepository extends JpaRepository<Wordpress, Integer>, QueryDslPredicateExecutor<Wordpress> {
	
	@RestResource(exported=false)
	Wordpress findByStation(Station station);

	Wordpress findByToken(@Param("token") String token);
}