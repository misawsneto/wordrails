package co.xarx.trix.persistence;

import co.xarx.trix.domain.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface UserConnectionRepository extends JpaRepository<UserConnection, Integer>, QueryDslPredicateExecutor<UserConnection> {

	@RestResource(exported = false)
	UserConnection findByProviderIdAndProviderUserId(@Param("providerId") String providerId, @Param("providerUserId") String providerUserId);

}
