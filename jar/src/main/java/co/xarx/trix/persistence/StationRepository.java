package co.xarx.trix.persistence;

import co.xarx.trix.domain.Station;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface StationRepository extends JpaRepository<Station, Integer>, QueryDslPredicateExecutor<Station> {
	List<Station> findByName(@Param("name") String name);

	@RestResource(exported=false)
	@Query(value="SELECT CASE WHEN (count(st) > 0) then true else false end FROM Station st WHERE st.id = :stationId AND st.visibility = 'UNRESTRICTED'")
	boolean isUnrestricted(@Param("stationId") Integer stationId);

	@Query("select station from Station station where station.id IN(select station.id from Station station join station.personsStationRoles personRoles join personRoles.person person where person.id = :personId and :networkId = station.network.id)or station.id IN(select station.id from Station station where(station.visibility = 'UNRESTRICTED' or station.visibility = 'RESTRICTED_TO_NETWORKS')and :networkId = station.network.id")
	List<Station> findByPersonIdAndNetworkId(@Param("personId") Integer personId, @Param("networkId") Integer networkId);

	@Query("select station from Station station join station.personsStationRoles personRoles join personRoles.person person " +
			"where person.id = :personId and station.id = :stationId")
	@RestResource(exported=false)
	Station belongsToStation(@Param("personId") Integer personId, @Param("stationId") Integer stationId);

	@Query("select station from Station station join station.personsStationRoles personRoles join personRoles.person person " +
			"where person.id = :personId and station.id IN (:stationsId)")
	@RestResource(exported=false)
	List<Station> belongsToStations(@Param("personId") Integer personId, @Param("stationsId") List<Integer> stationsId);

	@RestResource(exported=false)
	@Modifying
	@Query(nativeQuery=true, value="DELETE FROM station_network WHERE stations_id = ?")
	void deleteStationNetwork(Integer stationId);


	@Query(value="select s.id, s.name, sum(post.favoritesCount), sum(post.readsCount), sum(post.recommendsCount), sum(post.commentsCount), sum(post.prcount)" +
			" from station as s" +
			" inner join (select post.id as pid, post.station_id, count(postread.post_id) as prcount, post.favoritesCount, post.readsCount, post.recommendsCount, post.commentsCount from post left join postread on postread.post_id = post.id group by post.id) post on s.id = post.station_id" +
			" where defaultPerspectiveId = :defaultPerspectiveId" +
			" group by s.id", nativeQuery = true)
	@RestResource(exported=false)
	List<Object[]> findAllWithCounts(@Param("defaultPerspectiveId") Integer defaultPerspectiveId);

	@RestResource(exported=false)
	@Query("select network.stations from Network network where network.id = :networkId")
	List<Station> findByNetworkId(@Param("networkId") Integer networkId);

	@RestResource(exported=false)
	@Query("select str.station from StationRole str where str.id in (:stationRolesIds) group by str.station.id")
	List<Station> findByStationRolesIds(@Param("stationRolesIds") List<Integer> stationRolesIds);

}