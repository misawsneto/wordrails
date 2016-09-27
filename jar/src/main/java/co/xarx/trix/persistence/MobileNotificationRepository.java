package co.xarx.trix.persistence;

import co.xarx.trix.domain.MobileNotification;
import jdk.Exported;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface MobileNotificationRepository extends JpaRepository<MobileNotification, Integer>,
		QueryDslPredicateExecutor<MobileNotification> {

	@Query("select n from MobileNotification n, MobileDevice device " +
			"where n.regId = device.deviceCode " +
			"and device.person.id = :personId " +
			"order by n.id desc")
	List<MobileNotification> findNotificationsByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);

//	@Modifying
//	@RestResource(exported = false)
//	@Query("update MobileNotification SET seen = 1 WHERE messageId = :messageId")
//	void setNotificationSeen(@Param("messageId") String messageId);

	MobileNotification findByMessageId(String messageId);
}