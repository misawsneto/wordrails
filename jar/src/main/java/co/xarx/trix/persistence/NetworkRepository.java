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

public interface NetworkRepository extends JpaRepository<Network, Integer>, QueryDslPredicateExecutor<Network> {

	@Query("from Network where id > :id order by id desc")
	List<Network> findNetworksOrderDesc(@Param("id") Integer id);

	@Query("select n.id from Network n join n.stations s where s.id = :stationId")
	@RestResource(exported = false)
	List<Integer> findIdsByStation(@Param("stationId") Integer stationId);

	@Query("select network from Network network " +
			"join network.personsNetworkRoles personRoles " +
			"join personRoles.person person " +
			"where person.id = :personId and network.id IN (:networksId)")
	@RestResource(exported = false)
	List<Network> belongsToNetworks(@Param("personId") Integer personId, @Param("networksId") List<Integer> networksId);

	@RestResource(exported = false)
	Network findNetworkBySubdomain(@Param("subdomain") String subdomain);

	@RestResource(exported = true)
//	@Cacheable(value = "network_findOneBySubdomain", key = "#p0")
	public Network findOneBySubdomain(@Param("subdomain") String subdomain);

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
			"(select count(*) from PersonNetworkRegId regId where regId.network.id = :networkId), " +
			"(select count(*) from PersonNetworkToken token where token.network.id = :networkId)" +
			" from Station s where s.network.id = :networkId")
	List<Object[]> findNetworkStats(@Param("networkId") Integer networkId);
}