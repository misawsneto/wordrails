package co.xarx.trix.persistence;

import co.xarx.trix.domain.UserConnection;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface UserConnectionRepository extends DatabaseRepository<UserConnection, Integer> {

	@RestResource(exported = false)
	UserConnection findByProviderIdAndProviderUserId(@Param("providerId") String providerId, @Param("providerUserId") String providerUserId);

}
