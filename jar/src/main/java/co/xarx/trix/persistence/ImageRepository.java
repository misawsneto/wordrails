package co.xarx.trix.persistence;

import co.xarx.trix.domain.Post;
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
	List<Image> findByPost(Post post);

	@Query("select image from Image image " +
			"join image.original original join image.small small join image.medium medium join image.large large " +
			"where " +
			"original.id = :fileId OR small.id = :fileId OR medium.id = :fileId OR large.id = :fileId")
	@RestResource(exported = false)
	List<Image> findByFileId(@Param("fileId") Integer fileId);

	@RestResource(exported = false)
	void deleteByPersonId(Integer id);


	@RestResource(exported = false)
	@Query("select image from Image image " +
			"left join image.pictures pictures " +
			"left join pictures.file file " +
			"where file.id = :fileId")
	Set<Image> findByFileIdFetchPictures(@Param("fileId") Integer fileId);

	@RestResource(exported = false)
	@Query("select image from Image image " +
			"left join fetch image.pictures picture " +
			"left join fetch picture.file file " +
			"where file.hash = :hash and file.networkId = :networkId")
	Set<Image> findByFileHashFetchPictures(@Param("hash") String hash, @Param("networkId") Integer networkId);

	@Modifying
	@RestResource(exported = false)
	@Query("DELETE FROM Image image where image.id in (:ids)")
	void deleteImages(@Param("ids") List<Integer> ids);

}