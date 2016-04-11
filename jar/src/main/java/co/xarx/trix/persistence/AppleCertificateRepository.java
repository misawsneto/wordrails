package co.xarx.trix.persistence;

import co.xarx.trix.domain.AppleCertificate;
import co.xarx.trix.persistence.custom.SingleInstancePerTenantRepository;

public interface AppleCertificateRepository extends DatabaseRepository<AppleCertificate, Integer>,
		SingleInstancePerTenantRepository<AppleCertificate> {
}
