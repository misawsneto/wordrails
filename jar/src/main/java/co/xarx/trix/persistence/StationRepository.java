package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Station;
import co.xarx.trix.persistence.custom.StationRepositoryCustom;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface StationRepository extends StationRepositoryCustom, JpaRepository<Station, Integer>,
		QueryDslPredicateExecutor<Station> {


	@Override
	@SdkExclude
	@RestResource(exported = true)
	@CacheEvict(value = {"stationsIds", "stations"})
	Station save(Station station);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Page<Station> findAll(Pageable pageable);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Station findOne(Integer id);

	@Override
	@SdkExclude
	@CacheEvict(value = {"stationsIds", "stations"})
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	void delete(Station user);

	@RestResource(exported = false)
	@Cacheable(value = "stationsIds", key = "#p0")
	@Query("select station.id from Station station where station.tenantId = :tenantId")
	List<Integer> findIds(@Param("tenantId") String tenantId);

	@RestResource(exported=false)
	@Query(value="SELECT CASE WHEN (count(st) > 0) then true else false end FROM Station st WHERE st.id = :stationId AND st.visibility = 'UNRESTRICTED'")
	boolean isUnrestricted(@Param("stationId") Integer stationId);

}