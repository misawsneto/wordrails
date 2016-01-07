package co.xarx.trix.persistence;

import co.xarx.trix.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;
import java.util.Set;

public interface ImageRepository extends JpaRepository<Image, Integer>, QueryDslPredicateExecutor<Image> {

	@RestResource(exported = false)
	@Query("select image from Image image " +
			"left join fetch image.pictures picture " +
			"left join fetch picture.file file " +
			"where file.hash = :hash")
	Set<Image> findByFileHashFetchPictures(@Param("hash") String hash);

	@Modifying
	@RestResource(exported = false)
	@Query("DELETE FROM Image image where image.id in (:ids)")
	void deleteImages(@Param("ids") List<Integer> ids);

}