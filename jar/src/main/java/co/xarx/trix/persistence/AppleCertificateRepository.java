package co.xarx.trix.persistence;

import co.xarx.trix.domain.AppleCertificate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = false)
public interface AppleCertificateRepository extends JpaRepository<AppleCertificate, Integer>, QueryDslPredicateExecutor<AppleCertificate> {

	@RestResource(exported = false)
	AppleCertificate findByTenantId(@Param("tenantId") String tenantId);
}
