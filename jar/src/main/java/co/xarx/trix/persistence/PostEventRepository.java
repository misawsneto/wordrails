package co.xarx.trix.persistence;

import co.xarx.trix.domain.PostEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface PostEventRepository extends JpaRepository<PostEvent, Integer>, QueryDslPredicateExecutor<PostEvent> {
}
