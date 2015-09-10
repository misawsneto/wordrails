package com.wordrails.persistence;

import com.wordrails.business.PostScheduled;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface PostScheduledRepository extends JpaRepository<PostScheduled, Integer>, QueryDslPredicateExecutor<PostScheduled> {

}