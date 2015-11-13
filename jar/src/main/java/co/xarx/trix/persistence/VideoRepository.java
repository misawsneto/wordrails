package co.xarx.trix.persistence;

import co.xarx.trix.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface VideoRepository extends JpaRepository<Video, Integer>, QueryDslPredicateExecutor<Video> {
}
