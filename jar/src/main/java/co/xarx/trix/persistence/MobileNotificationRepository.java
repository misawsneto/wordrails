package co.xarx.trix.persistence;

import co.xarx.trix.domain.MobileNotification;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MobileNotificationRepository extends DatabaseRepository<MobileNotification, Integer> {

	@Query("select n from MobileNotification n, MobileDevice device " +
			"where n.regId = device.deviceCode " +
			"and device.person.id = :personId " +
			"order by n.id desc")
	List<MobileNotification> findNotificationsByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);
}