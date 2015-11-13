package com.wordrails.persistence;

import com.wordrails.business.UserGrantedAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface UserGrantedAuthorityRepository extends JpaRepository<UserGrantedAuthority, Integer>, QueryDslPredicateExecutor<UserGrantedAuthority> {
}
