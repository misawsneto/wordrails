package com.wordrails.persistence;

import com.wordrails.business.PostTrash;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PostTrashRepository extends JpaRepository<PostTrash, Integer>, QueryDslPredicateExecutor<PostTrash> {
}