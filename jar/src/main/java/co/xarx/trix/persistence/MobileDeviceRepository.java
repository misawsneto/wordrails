package co.xarx.trix.persistence;

import co.xarx.trix.domain.MobileDevice;
import org.hibernate.annotations.NamedQuery;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.lang.annotation.Native;
import java.util.List;

public interface MobileDeviceRepository extends DatabaseRepository<MobileDevice, Integer> {

	@RestResource(exported = false)
	@Query("SELECT device FROM MobileDevice device " +
			"where device.person.id in (:personIds)")
	List<MobileDevice> findByPersonIds(@Param("personIds") List<Integer> personIds);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);

	@RestResource(exported = false)
	@Query("select count(*) from MobileDevice md where md.type = 0 and tenantId = :tenantId")
	Object countAndroidDevices(@Param("tenantId") String tenantId);

	@RestResource(exported = false)
	@Query("select count(*) from MobileDevice md where md.type = 1 and tenantId = :tenantId")
	Object countAppleDevices(@Param("tenantId") String tenantId);
}