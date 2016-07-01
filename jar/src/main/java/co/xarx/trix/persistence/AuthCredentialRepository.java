package co.xarx.trix.persistence;

import co.xarx.trix.domain.AuthCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface AuthCredentialRepository extends JpaRepository<AuthCredential, Integer> {

	@Query("SELECT ac from AuthCredential ac where ac.tenantId = :tenantId")
	AuthCredential findAuthCredentialByTenantId(@Param("tenantId") String tenantId);
}