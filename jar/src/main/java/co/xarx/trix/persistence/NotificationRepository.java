package co.xarx.trix.persistence;

import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RestResource;

public interface NotificationRepository extends JpaRepository<Notification, Integer>, QueryDslPredicateExecutor<Notification> {

	@Override
	@RestResource(exported = false)
	<S extends Notification> S save(S arg0);

//	@Query("select notification from Notification notification " +
//			"inner join MobileDevice mobileDevice on mobileDevice.deviceCode=notification.regId " +
//			"where mobileDevice.person.id = :personId order by notification.id desc")
//	List<Notification> findNotificationsByPersonIdOrderByDate(@Param("personId") Integer personId);

	@RestResource(exported = false)
	void deleteByPost(Post post);

}