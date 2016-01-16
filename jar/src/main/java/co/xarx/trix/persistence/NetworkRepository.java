package co.xarx.trix.persistence;

import co.xarx.trix.domain.Network;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface NetworkRepository extends JpaRepository<Network, Integer>, QueryDslPredicateExecutor<Network> {

	@Query("from Network where id > :id order by id desc")
	List<Network> findNetworksOrderDesc(@Param("id") Integer id);

	@RestResource(exported = false)
	@Query("select n.id from Network n join n.stations s where s.id = :stationId")
	List<Integer> findIdsByStation(@Param("stationId") Integer stationId);

	@RestResource(exported = false)
	@Query("select network from Network network " +
			"join network.personsNetworkRoles personRoles " +
			"join personRoles.person person " +
			"where person.id = :personId and network.id IN (:networksId)")
	List<Network> belongsToNetworks(@Param("personId") Integer personId, @Param("networksId") List<Integer> networksId);

	@RestResource(exported = false)
	@Query("select tenantId from Network")
	Set<String> findTenantIds();

	@RestResource(exported = false)
	@Query("select domain, tenantId from Network")
	Map<String, String> findDomains();

	@RestResource(exported = false)
//	@Cacheable(value = "network_findByDomain", key = "#p0")
	public Network findByDomain(String domain);

	@Override
	@RestResource(exported = false)
//	@Cacheable(value = "network_findOne", key = "#p0")
	public Network findOne(Integer integer);

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