package com.wordrails.persistence;

import com.wordrails.business.FileContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface FileContentsRepository extends JpaRepository<FileContents, Integer>, QueryDslPredicateExecutor<FileContents> {

	@Query("select hash from File where id=:id and type='E'")
	String findExternalHashById(@Param("id") Integer id);

	FileContents findByHashAndNetworkId(@Param("hash") String hash, @Param("networkId") Integer networkId);
}