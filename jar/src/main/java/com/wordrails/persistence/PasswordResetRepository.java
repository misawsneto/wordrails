package com.wordrails.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.domain.PasswordReset;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer>, QueryDslPredicateExecutor<PasswordReset> {
	@RestResource(exported=true)
	public PasswordReset findByHash(@Param("hash") String hash);
}