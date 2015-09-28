package com.wordrails.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.wordrails.business.Sponsor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SponsorRepository extends JpaRepository<Sponsor, Integer>, QueryDslPredicateExecutor<Sponsor> {

    public List<Sponsor> findSponsorByNetworkId(@Param("networkId") Integer networkId);
}