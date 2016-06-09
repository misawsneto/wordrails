package co.xarx.trix.persistence;

import co.xarx.trix.domain.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource(exported = true)
public interface NetworkRepository extends JpaRepository<Network, Integer>,
		QueryDslPredicateExecutor<Network> {

	@RestResource(exported = true)
	Network findByTenantId(@Param("tenantId") String tenantId);

	@RestResource(exported = false)
	Network findByDomain(@Param("domain") String domain);
}