package co.xarx.trix.persistence;

import co.xarx.trix.domain.Network;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface NetworkRepository extends JpaRepository<Network, Integer>, QueryDslPredicateExecutor<Network> {

	@RestResource(exported = true)
	Network findByTenantId(@Param("tenantId") String tenantId);

	@RestResource(exported = false)
	Network findByDomain(@Param("domain") String domain);

	@RestResource(exported = false)
	@Query(value = "select " +
			"(select count(*) from PostRead pr where pr.post.stationId = s.id), " +
			"(select count(*) from Comment comment where comment.post.stationId = s.id), " +
			"(select count(*) from Recommend recommend where recommend.post.stationId = s.id)," +
			"(select count(*) from PersonNetworkRegId regId), " +
			"(select count(*) from PersonNetworkToken token)" +
			" from Station s")
	List<Object[]> findStats();
}