package co.xarx.trix.persistence;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Station;
import co.xarx.trix.domain.StationRole;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface StationRolesRepository extends JpaRepository<StationRole, Integer>, QueryDslPredicateExecutor<StationRole> {

	@RestResource(exported=false)
	StationRole findByStationAndPerson(Station station, Person person);

	@RestResource(exported=false)
	StationRole findByStationAndPersonId(Station station, Integer personId);

	StationRole findByStationIdAndPersonId(@Param("stationId") Integer stationId, @Param("personId") Integer personId);

	@RestResource(exported=false)
	List<StationRole> findByStation(Station station);

	@Query("select role from StationRole role " +
			"join role.person person " +
			"join role.station station " +
			"where person.id = :personId and :networkId = station.network.id")
	@RestResource(exported=false)
	List<StationRole> findByPersonIdAndNetworkId(@Param("personId") Integer personId, @Param("networkId") Integer networkId);

	@Query("SELECT str FROM StationRole str WHERE str.station.id in (:stationIds) order by str.person.id desc")
	List<StationRole> findRolesByStationIds(@Param("stationIds") List<Integer> stationIds, Pageable pageable);

	@RestResource(exported = false)
	@Query("count(*) FROM StationRole str WHERE str.station.id in (:stationIds) AND (str.person.name = :nameOrUsernameOrEmail OR str.person.username = :nameOrUsernameOrEmail OR str.person.email = :nameOrUsernameOrEmail) order by str.person.id desc")
	Long countRolesByStationIds(@Param("stationIds") List<Integer> stationIds);

	@Query("SELECT str FROM StationRole str WHERE str.station.id in (:stationIds) AND (str.person.name = :nameOrUsernameOrEmail OR str.person.username = :nameOrUsernameOrEmail OR str.person.email = :nameOrUsernameOrEmail) order by str.person.id desc")
	List<StationRole> findRolesByStationIdsAndNameOrUsernameOrEmail(@Param("stationIds") List<Integer> stationIds, @Param("nameOrUsernameOrEmail") String nameOrUsernameOrEmail, Pageable pageable);

	@RestResource(exported = false)
	@Query("count(*) FROM StationRole str WHERE str.station.id in (:stationIds) AND (str.person.name = :nameOrUsernameOrEmail OR str.person.username = :nameOrUsernameOrEmail OR str.person.email = :nameOrUsernameOrEmail) order by str.person.id desc")
	Long countRolesByStationIdsAndNameOrUsernameOrEmail(@Param("stationIds") List<Integer> stationIds, @Param("nameOrUsernameOrEmail") String nameOrUsernameOrEmail);

	@RestResource(exported=false)
	@Query("SELECT str FROM StationRole str WHERE str.station.id in (:stationIds) AND str.person.id = :personId")
	List<StationRole> findByPersonAndStationIds(@Param("personId") Integer personId, @Param("stationIds") List<Integer> stationIds);

	@RestResource(exported=false)
	@Modifying
	@Query("delete from StationRole str where str.id in (:stationRolesIds)")
	void deleteByIds(@Param("stationRolesIds") List<Integer> stationRolesIds);

	@RestResource(exported = false)
	@Modifying
	void deleteRolesByPersonId(Integer id);
}