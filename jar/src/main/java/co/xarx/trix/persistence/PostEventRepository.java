package co.xarx.trix.persistence;

import co.xarx.trix.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface PostEventRepository extends JpaRepository<Event, Integer>, QueryDslPredicateExecutor<Event> {
}