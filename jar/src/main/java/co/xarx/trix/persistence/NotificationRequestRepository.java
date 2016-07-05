package co.xarx.trix.persistence;

import co.xarx.trix.domain.NotificationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface NotificationRequestRepository extends JpaRepository<NotificationRequest, Integer>,
		QueryDslPredicateExecutor<NotificationRequest> {
}