package co.xarx.trix.persistence;

import co.xarx.trix.domain.File;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Set;

public interface PictureRepository extends JpaRepository<Picture, Integer>, QueryDslPredicateExecutor<Picture> {

	@RestResource(exported = false)
	@Query("from Picture p where p.file.hash = :hash")
	Picture findByFileHash(@Param("hash") String hash);

	@RestResource(exported = false)
	Picture findByFile(@Param("file") File file);

	@RestResource(exported = false)
	Set<Picture> findByImage(@Param("image") Image image);
}