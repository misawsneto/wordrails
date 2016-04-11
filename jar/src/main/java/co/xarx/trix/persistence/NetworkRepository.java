package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Network;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface NetworkRepository extends DatabaseRepository<Network, Integer> {

	@RestResource(exported = true)
	Network findByTenantId(@Param("tenantId") String tenantId);

	@RestResource(exported = false)
	Network findByDomain(@Param("domain") String domain);

	@RestResource(exported = false)
	@Query(value = "select " +
			"(select count(*) from PostRead pr where pr.post.stationId = s.id), " +
			"(select count(*) from Comment comment where comment.post.stationId = s.id), " +
			"(select count(*) from MobileDevice md where md.type = 0), " +
			"(select count(*) from MobileDevice md where md.type = 1)" +
			" from Station s")
	List<Object[]> findStats();

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Network save(Network entity);
}