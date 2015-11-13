package com.wordrails.persistence;

import com.wordrails.domain.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface AdRepository extends JpaRepository<Ad, Integer>, QueryDslPredicateExecutor<Ad> {
}