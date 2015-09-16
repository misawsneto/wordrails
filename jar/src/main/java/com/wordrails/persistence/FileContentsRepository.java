package com.wordrails.persistence;

import com.wordrails.business.File;
import com.wordrails.business.FileContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface FileContentsRepository extends JpaRepository<FileContents, Integer>, QueryDslPredicateExecutor<FileContents> {

	File findByHash(@Param("hash") String hash, @Param("networkId") Integer networkId);
}