package co.xarx.trix.persistence;

import co.xarx.trix.domain.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;
import java.util.List;

public interface UserConnectionRepository extends JpaRepository<UserConnection, Integer>, QueryDslPredicateExecutor<UserConnection> {

	@RestResource(exported = false)
	@Query("select uc from UserConnection uc where uc.providerId = :providerId and uc.user.network.id = :networkId")
	List<UserConnection> findByProviderId(@Param("providerId") String providerId,
	                                                  @Param("networkId") Integer networkId);

	@RestResource(exported = false)
	@Query("select uc from UserConnection uc where uc.providerId = :providerId and uc.providerUserId = :providerUserId and uc.user.network.id = :networkId")
	UserConnection findByProviderIdAndProviderUserId(@Param("providerId") String providerId,
	                                                 @Param("providerUserId") String providerUserId,
	                                                 @Param("networkId") Integer networkId);

	@RestResource(exported = false)
	UserConnection findByProviderIdAndProviderUserId(@Param("providerId") String providerId,
	                                                 @Param("providerUserId") String providerUserId);

	@RestResource(exported = false)
	@Query("select uc from UserConnection uc where " +
			"(uc.providerUserId in (:fbIds) and uc.providerId in ('facebook')) OR" +
			"(uc.providerUserId in (:ttIds) and uc.providerId in ('twitter')) OR" +
			"(uc.providerUserId in (:ggIds) and uc.providerId in ('google')) AND uc.user.network.id = :networkId")
	List<UserConnection> findByProvidersAndUserId(@Param("fbIds") List<String> fbIds,
	                                                   @Param("ttIds") List<String> ttIds,
	                                                   @Param("ggIds") List<String> ggIds,
	                                                   @Param("networkId") Integer networkId);

	@RestResource(exported = false)
	@Query("select uc from UserConnection uc where (uc.providerUserId in (:userIds) and uc.providerId in (:providerId)) AND uc.user.network.id = :networkId")
	List<UserConnection> findByProviderIdAndUserIds(@Param("providerId") String providerId,
	                                                @Param("userIds") Collection<String> userIds,
	                                                @Param("networkId") Integer networkId);

	@RestResource(exported = false)
	@Query("select uc from UserConnection uc where (uc.providerUserId in (:userIds) and uc.providerId in (:providerId))")
	List<UserConnection> findByProviderIdAndUserIds(@Param("providerId") String providerId,
	                                                @Param("userIds") Collection<String> userIds);

	@RestResource(exported = false)
	@Query("delete UserConnection uc where uc.providerId = :providerId and uc.user.network.id = :networkId")
	void deleteByProviderId(@Param("providerId") String providerId, @Param("networkId") Integer networkId);

	@RestResource(exported = false)
	@Query("delete UserConnection uc where uc.providerId = :providerId and uc.providerUserId = :providerUserId and uc.user.network.id = :networkId")
	void deleteByProviderIdAndProviderUserId(@Param("providerId") String providerId,
	                                         @Param("providerUserId") String providerUserId,
	                                         @Param("networkId") Integer networkId);

}
