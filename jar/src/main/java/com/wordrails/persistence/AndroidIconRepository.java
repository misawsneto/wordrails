package com.wordrails.persistence;

import com.wordrails.business.AndroidIcon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface AndroidIconRepository extends JpaRepository<AndroidIcon, Integer>, QueryDslPredicateExecutor<AndroidIcon> {
}