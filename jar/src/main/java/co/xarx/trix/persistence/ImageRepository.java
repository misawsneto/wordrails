package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Image;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface ImageRepository extends JpaRepository<Image, Integer>, QueryDslPredicateExecutor<Image> {

	@Modifying
	@RestResource(exported = false)
	@Query("DELETE FROM Image image where image.id in (:ids)")
	void deleteImages(@Param("ids") List<Integer> ids);

	@RestResource(exported = true)
	@Query("SELECT image FROM Image image ORDER BY image.id DESC")
	public List<Image> findImagesOrderByDate(Pageable pageable);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Image findOne(Integer id);
}