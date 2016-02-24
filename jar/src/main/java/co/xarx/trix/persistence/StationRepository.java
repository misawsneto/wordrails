package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Station;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Integer>, QueryDslPredicateExecutor<Station> {


	@RestResource(exported = false)
	@Cacheable(value = "stationsIds", key = "#p0")
	@Query("select id from Station station where tenantId = :tenantId")
	List<Integer> findIds(@Param("tenantId") String tenantId);

	@Override
	@SdkExclude
	@CacheEvict(value = "stationsIds")
	Station save(Station station);

	@Override
	@SdkExclude
	@CacheEvict(value = "stationsIds")
	void delete(Station user);



	@RestResource(exported=false)
	@Query(value="SELECT CASE WHEN (count(st) > 0) then true else false end FROM Station st WHERE st.id = :stationId AND st.visibility = 'UNRESTRICTED'")
	boolean isUnrestricted(@Param("stationId") Integer stationId);

	@RestResource(exported = false)
	@Query("select station from Station station join station.personsStationRoles personRoles join personRoles.person person where (person.id = :personId or station.visibility = 'UNRESTRICTED') GROUP BY station")
	List<Station> findByPersonId(@Param("personId") Integer personId);

	@RestResource(exported = false)
	@Query("select station from Station station join station.personsStationRoles personRoles join personRoles.person person " +
			"where person.id = :personId and station.id = :stationId")
	Station belongsToStation(@Param("personId") Integer personId, @Param("stationId") Integer stationId);

	@RestResource(exported = false)
	@Query("select station from Station station join station.personsStationRoles personRoles join personRoles.person person " +
			"where person.id = :personId and station.id IN (:stationsId)")
	List<Station> belongsToStations(@Param("personId") Integer personId, @Param("stationsId") List<Integer> stationsId);

	@RestResource(exported=false)
	@Modifying
	@Query(nativeQuery=true, value="DELETE FROM station_network WHERE stations_id = ?")
	void deleteStationNetwork(Integer stationId);

	@RestResource(exported=false)
	@Query("select str.station from StationRole str where str.id in (:stationRolesIds) group by str.station.id")
	List<Station> findByStationRolesIds(@Param("stationRolesIds") List<Integer> stationRolesIds);

}