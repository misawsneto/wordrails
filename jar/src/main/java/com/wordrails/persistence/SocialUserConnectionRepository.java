package com.wordrails.persistence;

import com.wordrails.business.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface SocialUserConnectionRepository extends JpaRepository<UserConnection, Integer>, QueryDslPredicateExecutor<UserConnection> {

}
