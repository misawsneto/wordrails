package co.xarx.trix.persistence;

import co.xarx.trix.domain.MobileDevice;
import co.xarx.trix.util.Constants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface MobileDeviceRepository extends JpaRepository<MobileDevice, Integer>,
		QueryDslPredicateExecutor<MobileDevice> {

	@RestResource(exported = false)
	@Query("SELECT device FROM MobileDevice device " +
			"where device.person.id in (:personIds)")
	List<MobileDevice> findByPersonIds(@Param("personIds") List<Integer> personIds);

	@RestResource(exported = false)
	@Query("SELECT device.deviceCode FROM MobileDevice device " +
			"where device.person.id in (:personIds) and device.type = 'ANDROID'")
	List<String> findAndroids(@Param("personIds") List<Integer> personIds);

	@RestResource(exported = false)
	@Query("SELECT device.deviceCode FROM MobileDevice device " +
			"where device.person.id in (:personIds) and device.type = 'APPLE'")
	List<String> findApples(@Param("personIds") List<Integer> personIds);

	@RestResource(exported = false)
	@Query("SELECT device.deviceCode FROM MobileDevice device " +
			"where device.person.id in (:personIds) and device.type = 'FCM_ANDROID'")
	List<String> findAndroidFCMs(@Param("personIds") List<Integer> personIds);

	@RestResource(exported = false)
	@Query("SELECT device.deviceCode FROM MobileDevice device " +
			"where device.person.id in (:personIds) and device.type = 'FCM_APPLE'")
	List<String> findIOSFCMs(@Param("personIds") List<Integer> personIds);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);

	@Modifying
	@RestResource(exported = false)
	void deleteByLastPersonLoggedId(Integer id);

//	@Modifying
//	@Query("DELETE from Post post WHERE post.id in (:ids)")
//	void forceDeleteAll(@Param("ids") List<Integer> ids);

    @RestResource(exported = false)
    @Modifying
    @Query("DELETE FROM MobileDevice mobileDevice WHERE mobileDevice.deviceCode IN (:deviceCodes)")
    void deleteByDeviceCode(@Param("deviceCodes") List<String> deviceCodes);

	@RestResource(exported = false)
	@Query("select count(*) from MobileDevice md where md.type = 'ANDROID' and tenantId = :tenantId")
	Object countAndroidDevices(@Param("tenantId") String tenantId);

	@RestResource(exported = false)
	@Query("select count(*) from MobileDevice md where md.type = 'APPLE' and tenantId = :tenantId")
	Object countAppleDevices(@Param("tenantId") String tenantId);

	@RestResource(exported = false)
	@Query("select count(*) from MobileDevice md where md.type = :type and tenantId = :tenantId")
	Object countDevicesByTenantIdAndType(@Param("tenantId") String tenantId, @Param("type") Constants.MobilePlatform type);

	@RestResource(exported = false)
	@Query("SELECT count(*) FROM MobileDevice md WHERE md.type = :type AND (date(md.updatedAt) >= date(:start) AND date(md.updatedAt) <= date(:end)) AND md.tenantId = :tenantId")
	Object countActiveDevices(@Param("tenantId") String tenantId, @Param("type") Constants.MobilePlatform type, @Param("start") String start, @Param("end") String end);
}