package com.wordrails.persistence;

import com.wordrails.business.Network;
import com.wordrails.business.Station;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface StationRepository extends JpaRepository<Station, Integer>, QueryDslPredicateExecutor<Station> {
	List<Station> findByName(@Param("name") String name);
    
	@RestResource(exported=false)
	Station findByWordpressId(@Param("wordpressId") Integer wordpressId);
    
	@RestResource(exported=false)
	Station findByWordpressToken(@Param("wordpressToken") String wordpressToken);

	List<Station> findByPersonIdAndNetworkId(@Param("personId") Integer personId, @Param("networkId") Integer networkId);
	
	@RestResource(exported=false)
	Station belongsToStation(@Param("personId") Integer personId, @Param("stationId") Integer stationId);
	
	@RestResource(exported=false)
	List<Station> belongsToStations(@Param("personId") Integer personId, @Param("stationsId") List<Integer> stationsId);
	
	@RestResource(exported=false)
	@Modifying
	@Query(nativeQuery=true, value="DELETE FROM station_network WHERE stations_id = ?")
	public void deleteStationNetwork(Integer stationId);
    
    
    @Query(value="select s.id, s.name, sum(post.favoritesCount), sum(post.readsCount), sum(post.recommendsCount), sum(post.commentsCount), sum(post.prcount)" +
            " from station as s" +
            " inner join (select post.id as pid, post.station_id, count(postread.post_id) as prcount, post.favoritesCount, post.readsCount, post.recommendsCount, post.commentsCount from post left join postread on postread.post_id = post.id group by post.id) post on s.id = post.station_id" +
            " where defaultPerspectiveId = :defaultPerspectiveId" +
            " group by s.id", nativeQuery = true)
    @RestResource(exported=false)
    public List<Object[]> findAllWithCounts(@Param("defaultPerspectiveId") Integer defaultPerspectiveId);

    @RestResource(exported=false)
    @Query("select network.stations from Network network where network.id = :networkId")
	public List<Station> findByNetworkId(@Param("networkId") Integer networkId);

	@RestResource(exported = false)
	@Query(nativeQuery=true, value="select name from network where id = (select networks_id from station_network where stations_id = ?)")
	public Network findByStationId(Integer stationId);

    @RestResource(exported=false)
    @Query("select str.station from StationRole str where str.id in (:stationRolesIds) group by str.station.id")
	public List<Station> findByStationRolesIds(@Param("stationRolesIds") List<Integer> stationRolesIds);

}