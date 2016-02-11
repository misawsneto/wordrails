package co.xarx.trix.persistence;

import co.xarx.trix.domain.MobileDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = false)
public interface MobileDeviceRepository extends JpaRepository<MobileDevice, Integer>, QueryDslPredicateExecutor<MobileDevice> {

	@RestResource(exported = false)
	@Query("SELECT device FROM MobileDevice device " +
			"join device.person person " +
			"join StationRole sr on sr.person = device.person " +
			"where sr.station.id = :stationId")
	List<MobileDevice> findByStation(@Param("stationId") Integer stationId);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);
}