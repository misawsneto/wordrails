package com.wordrails.persistence;

import com.wordrails.business.Wordpress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface WordpressRepository extends JpaRepository<Wordpress, Integer>, QueryDslPredicateExecutor<Wordpress> {

}