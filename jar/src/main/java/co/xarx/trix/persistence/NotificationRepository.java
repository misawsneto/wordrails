package co.xarx.trix.persistence;

import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer>, QueryDslPredicateExecutor<Notification> {

	@Override
	@RestResource(exported = false)
	<S extends Notification> S save(S arg0);

	@Query("select n from Notification n, MobileDevice device where n.regId = device.deviceCode and device.person.id = :personId")
	List<Notification> findNotificationsByPersonIdOrderByDate(@Param("personId") Integer personId);

	@RestResource(exported = false)
	void deleteByPost(Post post);

}