package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.persistence.custom.PostRepositoryCustom;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface PostRepository extends PostRepositoryCustom, JpaRepository<Post, Integer>,
		QueryDslPredicateExecutor<Post> {

	@PostAuthorize("hasPermission(returnObject, 'read') or returnObject==null")
	@Query("SELECT post FROM Post post where post.slug = :slug and post is not null")
	Post findBySlug(@Param("slug") String slug);

	@Override
	@SdkExclude
	@CacheEvict(value = "postsIds")
	@RestResource(exported = true)
	void delete(Integer id);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Post findOne(Integer id);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	@PostFilter("hasPermission(filterObject, 'read')")
	List<Post> findAll();

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Page<Post> findAll(Pageable pageable);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	@CacheEvict(value = "postsIds")
	<S extends Post> S save(S entity);

//	@Override
//	@SdkExclude
//	@RestResource(exported = true)
//	@CacheEvict(value = "postsIds")
//	<S extends Post> List<S> save(Iterable<S> entities);

	@Query("select post from Post post join post.terms t where " +
			"post.station.id = :stationId and t.id in (:termsIds) and post.state = 'PUBLISHED' group by post order by post.date desc")
	List<Post> findPostsPublished(@Param("stationId") Integer stationId, @Param("termsIds") List<Integer> termsIds, Pageable pageable);

	@Deprecated
	@Query("select pr.post.id from PostRead pr where pr.person.id=:personId")
	List<Integer> findPostReadByPerson(@Param("personId") Integer personId, Pageable pageable);

	@Query("SELECT post FROM Post post where post.station.id = :stationId and post.state = 'PUBLISHED' ORDER BY post.date DESC")
	List<Post> findPostsOrderByDateDesc(@Param("stationId") Integer stationId, Pageable pageable);

	// ---------------------------------- NOT EXPOSED ----------------------------------

	@RestResource(exported = false)
	@Query("select post from Post post " +
			"where post.id in " +
			"(select p.id from Post p join p.terms t where p.station.id = :stationId and t.id in (:termsIds) and p.id not in (:idsToExclude)) " +
			"order by post.date desc")
	List<Post> findPostsNotPositioned(@Param("stationId") Integer stationId, @Param("termsIds") List<Integer> termsIds, @Param("idsToExclude") List<Integer> idsToExclude, Pageable pageable);

	@RestResource(exported = false)
	@Query("select post.id from Post post where post.tenantId = :tenantId")
	@Cacheable(value = "postsIds", key = "#p0")
	List<Integer> findIds(@Param("tenantId") String tenantId);

	@Modifying
	@RestResource(exported = false)
	@Query("UPDATE Post p set p.readsCount = p.readsCount + 1 where p.id = :postId")
	void incrementReadCount(@Param("postId") int postId);

	@RestResource(exported = false)
	List<Post> findByStation(Station station);

	@RestResource(exported = false)
	@Query("select post from Post post join post.station station " +
			"where station.id = :stationId and post.id not in" +
			"(select pr.id from PostRead pr join pr.person person where person.id=:personId)")
	List<Post> findPostReadByStationAndPerson(@Param("stationId") Integer stationId, @Param("personId") Integer personId, Pageable pageable);

	@RestResource(exported = false)
	@Query("select post from Post post join post.station station where " +
			"station.id = :stationId and post.id not in " +
			"(select pr.id from PostRead pr join pr.person person where person.id=:personId)")
	List<Post> findPostReadByStationAndPerson(@Param("stationId") Integer stationId, @Param("personId") Integer personId);

	@RestResource(exported = false)
	@Query("SELECT post FROM Post post where post.station.id = :stationId AND post.state = 'PUBLISHED' ORDER BY post.readsCount DESC, post.id DESC")
	List<Post> findPopularPosts(@Param("stationId") Integer stationId, Pageable pageable);

	@RestResource(exported = false)
	@Query("SELECT post FROM Post post where " +
			"post.author.id = :personId AND post.state = 'PUBLISHED' AND post.station.id in (:stationIds) order by post.id DESC")
	List<Post> findPostByPersonIdAndStations(@Param("personId") Integer personId, @Param("stationIds") List<Integer> stationIds, Pageable pageable);

	@RestResource(exported = false)
	@Query("SELECT post FROM Post post where " +
			"post.author.id = :personId AND post.state = :state AND post.station.id in (:stationIds) order by post.id DESC")
	List<Post> findPostByPersonIdAndStationsAndState(@Param("personId") Integer personId, @Param("state") String state,
													 @Param("stationIds") List<Integer> stationIds, Pageable pageable);

	@RestResource(exported = false)
	@Query("select " +
			"(select count(*) from PostRead pr where pr.post.id = p.id), " +
				"(select count(*) from Comment comment where comment.post.id = p.id) " +
			"from Post p where p.id = :postId")
	List<Object[]> findPostStats(@Param("postId") Integer postId);

	@RestResource(exported = false)
	@Query("select post.body from Post post where post.id=:postId")
	String findPostBodyById(@Param("postId") Integer postId);

	@RestResource(exported = false)
	@Query("select post from Post post where post.author.id = :personId")
	List<Post> findAllFromPerson(@Param("personId") Integer personId);

	@RestResource(exported = false)
	@Query("select post from Post post join post.terms term where term.id in (:termId) and post.featuredImage is not null")
	List<Post> findByFeaturedImageByTermId(@Param("termId") Integer termId, Pageable page);

	@RestResource(exported = false)
	@Modifying
	@Query("DELETE from Post post WHERE post.id in (:ids)")
	void forceDeleteAll(@Param("ids") List<Integer> ids);

	@RestResource(exported = false)
	@Modifying
	@Query("DELETE from Post post WHERE post.id = :id")
	void forceDelete(@Param("id") Integer id);

	@RestResource(exported = true)
	List<Post> findByAuthorUsernameAndStateOrderByDateDesc(@Param("username") String username, @Param("state") String
			state, Pageable pageable);

	@RestResource(exported = false)
	@Query("SELECT post from Post post join fetch post.terms terms")
	List<Post> findPostWithTerms();
}