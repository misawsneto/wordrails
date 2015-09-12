package com.wordrails.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.wordrails.business.File;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<File, Integer>, QueryDslPredicateExecutor<File> {

	@Query("select hash from File where id=:id")
	String findHashById(@Param("id") Integer id);
}