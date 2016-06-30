package co.xarx.trix.persistence;

import co.xarx.trix.domain.PersonalNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface PersonalNotificationRepository extends JpaRepository<PersonalNotification, Integer>,
		QueryDslPredicateExecutor<PersonalNotification> {
}