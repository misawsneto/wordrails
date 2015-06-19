package com.wordrails.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.wordrails.business.File;

public interface FileRepository extends JpaRepository<File, Integer>, QueryDslPredicateExecutor<File> {
}