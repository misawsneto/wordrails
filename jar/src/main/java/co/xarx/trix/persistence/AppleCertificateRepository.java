package co.xarx.trix.persistence;

import co.xarx.trix.domain.AppleCertificate;
import co.xarx.trix.persistence.custom.SingleInstancePerTenantRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AppleCertificateRepository extends JpaRepository<AppleCertificate, Integer>,
		QueryDslPredicateExecutor<AppleCertificate>, SingleInstancePerTenantRepository<AppleCertificate> {
}
