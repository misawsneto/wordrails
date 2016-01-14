package co.xarx.trix.persistence;

import co.xarx.trix.domain.event.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface EventRepository extends JpaRepository<Event, Integer>, QueryDslPredicateExecutor<Event> {
}