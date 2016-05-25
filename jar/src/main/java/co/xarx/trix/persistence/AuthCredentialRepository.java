package co.xarx.trix.persistence;

import co.xarx.trix.domain.AuthCredential;
import org.springframework.data.repository.query.Param;

public interface AuthCredentialRepository extends DatabaseRepository<AuthCredential, Integer> {

	AuthCredential findByTenantId(@Param("tenantId") String tenantId);
}