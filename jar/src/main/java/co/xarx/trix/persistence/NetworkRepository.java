package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Network;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = true)
public interface NetworkRepository extends DatabaseRepository<Network, Integer> {

	@RestResource(exported = true)
	Network findByTenantId(@Param("tenantId") String tenantId);

	@RestResource(exported = false)
	Network findByDomain(@Param("domain") String domain);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Network save(Network entity);
}