package com.wordrails.persistence;

import com.wordrails.domain.Network;
import com.wordrails.domain.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface NetworkRepository extends JpaRepository<Network, Integer>, QueryDslPredicateExecutor<Network> {

	@Query("from Network where id > :id order by id desc")
	List<Network> findNetworksOrderDesc(@Param("id") Integer id);

	@RestResource(exported = false)
	Network findNetworkById(@Param("networkId") Integer networkId);

	@RestResource(exported = false)
	List<Network> findByStations(Station station);

	@RestResource(exported = false)
	List<Integer> findIdsByStation(@Param("stationId") Integer stationId);

	@RestResource(exported = false)
	List<Network> belongsToNetworks(@Param("personId") Integer personId, @Param("networksId") List<Integer> networksId);

	@RestResource(exported = true)
	List<Network> findBySubdomain(@Param("subdomain") String subdomain);

	@RestResource(exported = false)
	Network findNetworkBySubdomain(@Param("subdomain") String subdomain);

	@RestResource(exported = true)
	Network findOneBySubdomain(@Param("subdomain") String subdomain);

	@RestResource(exported = false)
	Network findByDomain(String domain);

	@RestResource(exported = false)
	@Query(value = "select " +
			"(select count(*) from PostRead pr where pr.post.stationId = s.id), " +
			"(select count(*) from Comment comment where comment.post.stationId = s.id), " +
			"(select count(*) from Recommend recommend where recommend.post.stationId = s.id)," +
			"(select count(*) from PersonNetworkRegId regId where regId.network.id = :networkId), " +
			"(select count(*) from PersonNetworkToken token where token.network.id = :networkId)" +
			" from Station s join s.networks n where n.id = :networkId")
	List<Object[]> findNetworkStats(@Param("networkId") Integer networkId);
//
//	@RestResource(exported = false)
//	Blob findCertificateIosById(@Param("networkId") Integer networkId);
//
//	@RestResource(exported = false)
//	String findCertificatePasswordById(@Param("stationId") Integer stationId);
}