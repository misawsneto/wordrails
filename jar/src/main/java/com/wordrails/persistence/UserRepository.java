package com.wordrails.persistence;

import com.wordrails.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

public interface UserRepository extends JpaRepository<User, Integer>, QueryDslPredicateExecutor<User> {

	@RestResource(exported = false)

	Set<User> findByUsernameAndEnabled(@Param("username") String username, @Param("enabled") boolean b);

	Set<User> findByNetworkIdAndEnabled(@Param("networkId") Integer networkId, @Param("enabled") boolean b);

	User findByUsernameAndEnabledAndNetworkId(@Param("username") String username, @Param("enabled") boolean b, @Param("networkId") Integer networkId);

	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN 'true' ELSE 'false' END FROM User u WHERE u.username = :username and network.id = :networkId")
	boolean existsByUsernameAndNetworkId(@Param("username") String username, @Param("networkId") Integer networkId);
}
