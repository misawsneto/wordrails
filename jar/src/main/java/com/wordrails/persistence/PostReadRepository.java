package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Notification;
import com.wordrails.business.PostRead;

public interface PostReadRepository extends JpaRepository<PostRead, Integer>, QueryDslPredicateExecutor<PostRead> {

	@Override
	@RestResource(exported = false)
	public <S extends PostRead> S save(S arg0);
	
	public List<Notification> findPostReadOrderByDate(@Param("personId") Integer personId);
}