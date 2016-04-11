package co.xarx.trix.persistence;

import co.xarx.trix.annotation.EventLoggableRepository;
import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.persistence.custom.CustomPostRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@EventLoggableRepository
public interface PostRepository extends JpaRepository<Post, Integer>, CustomPostRepository {

	@Modifying
	@RestResource(exported = false)
	@Query("UPDATE Post p set p.readsCount = p.readsCount + 1 where p.id = :postId")
	void incrementReadCount(@Param("postId") int postId);

	@Query("select post from Post post where post.id in ( select p.id from Post p where p.station.id = :stationId ) order by post.date desc")
	List<Post> findPostsFromOrPromotedToStation(@Param("stationId") int stationId, Pageable pageable);

	@Query("select post from Post post join post.terms t where " +
			"post.station.id = :stationId and t.id in (:termsIds) and post.state = 'PUBLISHED' group by post order by post.date desc")
	List<Post> findPostsPublished(@Param("stationId") Integer stationId, @Param("termsIds") List<Integer> termsIds, Pageable pageable);

	@Query("select post from Post post " +
			"where post.id in " +
			"(select p.id from Post p join p.terms t where p.station.id = :stationId and t.id in (:termsIds) and p.id not in (:idsToExclude)) " +
			"order by post.date desc")
	List<Post> findPostsNotPositioned(@Param("stationId") Integer stationId, @Param("termsIds") List<Integer> termsIds, @Param("idsToExclude") List<Integer> idsToExclude, Pageable pageable);

	@RestResource(exported = false)
	List<Post> findByStation(Station station);

	@RestResource(exported = false)
	@Modifying
	@Query(value = "UPDATE Post post SET post.featuredImage = null WHERE post.featuredImage IN (:featuredImages)")
	void updateFeaturedImagesToNull(@Param("featuredImages") Collection<Image> featuredImage);

	@RestResource(exported = false)
	@Query("select p from Post p where p.station.id = :stationId and p.author.id = :authorId order by p.date desc")
	List<Post> findPostsByStationIdAndAuthorId(@Param("stationId") Integer stationId, @Param("authorId") Integer authorId, Pageable pageable);

	@Query("select pr.post.id from PostRead pr where pr.person.id=:personId")
	List<Integer> findPostReadByPerson(@Param("personId") Integer personId, Pageable pageable);

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

	@Query("SELECT post FROM Post post where post.station.id = :stationId ORDER BY post.date DESC")
	List<Post> findPostsOrderByDateDesc(@Param("stationId") Integer stationId, Pageable pageable);

	@RestResource(exported = false)
	@Query("SELECT post FROM Post post where post.station.id = :stationId ORDER BY post.readsCount DESC, post.id DESC")
	List<Post> findPopularPosts(@Param("stationId") Integer stationId, Pageable pageable);

	Post findByOriginalPostId(@Param("originalPostId") Integer originalPostId);

	Post findBySlug(@Param("slug") String slug);

	List<Post> findPostBySlug(@Param("slug") String slug);

	@RestResource(exported = false)
	@Query("SELECT post FROM Post post ORDER BY post.date DESC")
	List<Post> findAllPostsOrderByIdDesc();

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
	@Query("SELECT post FROM Recommend recommend join recommend.post post where " +
			"recommend.person.id = :personId AND post.station.id in (:stationIds) order by recommend.id DESC")
	List<Post> findRecommendationsByPersonIdAndStations(Integer personId, List<Integer> stationIds, Pageable pageable);

	@RestResource(exported = false)
	@Query("select " +
			"(select count(*) from PostRead pr where pr.post.id = p.id), " +
			"(select count(*) from Comment comment where comment.post.id = p.id), " +
			"(select count(*) from Recommend recommend where recommend.post.id = p.id) " +
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
}