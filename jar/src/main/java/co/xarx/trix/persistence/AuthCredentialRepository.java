package co.xarx.trix.persistence;

import co.xarx.trix.domain.AuthCredential;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface AuthCredentialRepository extends DatabaseRepository<AuthCredential, Integer> {

	AuthCredential findAuthCredentialByTenantId(@Param("tenantId") String tenantId);
}