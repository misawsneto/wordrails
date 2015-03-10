package com.wordrails.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.PasswordReset;

public interface PasswordResetRepository extends JpaRepository<PasswordReset, Integer>, QueryDslPredicateExecutor<PasswordReset> {
	@RestResource(exported=false)
	public PasswordReset findByHash(String hash);
}