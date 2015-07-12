package com.wordrails.persistence;

import com.wordrails.business.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer>, QueryDslPredicateExecutor<User> {

	Set<User> findByUsernameAndEnabled(@Param("username") String username, @Param("enabled") boolean b);

	Set<User> findByNetworkIdAndEnabled(@Param("networkId") Integer networkId, @Param("enabled") boolean b);

	User findByUsernameAndEnabledAndNetworkId(@Param("username") String username, @Param("enabled") boolean b, @Param("networkId") Integer networkId);
}
