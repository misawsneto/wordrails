package co.xarx.trix.persistence;

import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface NotificationRepository extends DatabaseRepository<Notification, Integer> {

	@Query("select n from Notification n, MobileDevice device " +
			"where n.regId = device.deviceCode " +
			"and device.person.id = :personId " +
			"order by n.id desc")
	List<Notification> findNotificationsByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);

	@RestResource(exported = false)
	void deleteByPost(Post post);
}