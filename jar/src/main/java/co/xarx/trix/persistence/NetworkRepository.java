package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Network;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface NetworkRepository extends JpaRepository<Network, Integer>,
		QueryDslPredicateExecutor<Network> {

	@RestResource(exported = true)
	@Cacheable(value = "network", key = "#p0")
	Network findByTenantId(@Param("tenantId") String tenantId);

	@RestResource(exported = false)
	Network findByDomain(@Param("domain") String domain);

	@RestResource(exported = false)
	@Query("select n.id, n.domain, n.tenantId from Network n")
	List<Object[]> findIdsAndDomain();

	@Override
	@SdkExclude
	@RestResource(exported = true)
	@CacheEvict(value = "network", key = "#p0.tenantId")
	Network save(Network network);
}