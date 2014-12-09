package com.wordrails.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.wordrails.business.Sponsor;

public interface SponsorRepository extends JpaRepository<Sponsor, Integer>, QueryDslPredicateExecutor<Sponsor> {
}