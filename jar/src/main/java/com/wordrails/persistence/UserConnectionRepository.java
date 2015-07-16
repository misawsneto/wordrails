package com.wordrails.persistence;

import com.wordrails.business.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public interface UserConnectionRepository extends JpaRepository<UserConnection, Integer>, QueryDslPredicateExecutor<UserConnection> {

	@RestResource(exported = false)
	List<UserConnection> findByProviderId(@Param("providerId") String providerId);

	@RestResource(exported = false)
	UserConnection findByProviderIdAndProviderUserId(@Param("providerId") String providerId, @Param("providerUserId") String providerUserId);

	@RestResource(exported = false)
	@Query("select sc from UserConnection sc where " +
			"(sc.providerUserId in (:fbIds) and sc.providerId in ('facebook')) OR" +
			"(sc.providerUserId in (:ttIds) and sc.providerId in ('twitter')) OR" +
			"(sc.providerUserId in (:ggIds) and sc.providerId in ('google'))")
	List<UserConnection> findByProvidersAndUserId(@Param("fbIds") List<String> fbIds,
	                                                   @Param("ttIds") List<String> ttIds,
	                                                   @Param("ggIds") List<String> ggIds);

	@RestResource(exported = false)
	@Query("select sc from UserConnection sc where (sc.providerUserId in (:userIds) and sc.providerId in (:providerId))")
	List<UserConnection> findByProviderIdAndUserIds(@Param("providerId") String providerId, @Param("userIds") Collection<String> userIds);

	@RestResource(exported = false)
	void deleteByProviderId(@Param("providerId") String providerId);

	@RestResource(exported = false)
	void deleteByProviderIdAndProviderUserId(@Param("providerId") String providerId, @Param("providerUserId") String providerUserId);

}
