package com.wordrails.persistence;

import com.wordrails.business.AndroidApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface AndroidAppRepository extends JpaRepository<AndroidApp, Integer>, QueryDslPredicateExecutor<AndroidApp> {
}