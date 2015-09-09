package com.wordrails.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.wordrails.business.TrixFile;
import org.springframework.data.repository.query.Param;

public interface FileRepository extends JpaRepository<TrixFile, Integer>, QueryDslPredicateExecutor<TrixFile> {

	@Query("select hash from TrixFile where id=:id")
	String findHashById(@Param("id") Integer id);
}