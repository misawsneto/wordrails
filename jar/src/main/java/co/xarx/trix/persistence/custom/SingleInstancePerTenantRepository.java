package co.xarx.trix.persistence.custom;

import co.xarx.trix.domain.MultiTenantEntity;

public interface SingleInstancePerTenantRepository<T extends MultiTenantEntity> {

	T findByTenantId(String tenantId);
}
