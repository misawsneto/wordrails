package co.xarx.trix.persistence;

import co.xarx.trix.domain.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface PictureRepository extends JpaRepository<Picture, Integer>, QueryDslPredicateExecutor<Picture> {
}