package com.wordrails.persistence;

import com.wordrails.business.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Set;

public interface PostRepository extends JpaRepository<Post, Integer>, QueryDslPredicateExecutor<Post> {
	List<Post> findPostsFromOrPromotedToStation(@Param("stationId") int stationId, Pageable pageable);

	List<Post> findPosts(@Param("stationId") Integer stationId, @Param("termId") Integer termId, Pageable pageable);

	List<Post> findPostsAndPostsPromoted(@Param("stationId") Integer stationId, @Param("termsIds") List<Integer> termsIds, Pageable pageable);

	List<Post> findPostsNotPositioned(@Param("stationId") Integer stationId, @Param("termsIds") List<Integer> termsIds, @Param("idsToExclude") List<Integer> idsToExclude, Pageable pageable);

	@RestResource(exported = false)
	List<Post> findByFeaturedImages(@Param("featuredImages") List<Image> featuredImage);

	@RestResource(exported = false)
	List<Post> findByStation(Station station);

	@RestResource(exported = false)
	Post findByWordpressId(Integer wordpressId);

	@RestResource(exported = false)
	List<Post> findPostsAndPostsPromotedByBody(@Param("stationId") Integer stationId, @Param("body") String body, Pageable pageable);

	@RestResource(exported = false)
	List<Post> findPostsAndPostsPromotedByTermId(@Param("stationId") Integer stationId, @Param("termId") Integer termId, Pageable pageable);

	@RestResource(exported = false)
	List<Post> findPostsAndPostsPromotedByAuthorId(@Param("stationId") Integer stationId, @Param("authorId") Integer authorId, Pageable pageable);

	@RestResource(exported = false)
	int countSlugPost(@Param("slug") String slug);

	@RestResource(exported = false)
	@Modifying
	@Query(value = "UPDATE Post post SET post.featuredImage = null WHERE post.featuredImage IN (:featuredImages)")
	void updateFeaturedImagesToNull(@Param("featuredImages") List<Image> featuredImage);

	@RestResource(exported = false)
	List<Post> findPostsByStationIdAndAuthorId(@Param("stationId") Integer stationId, @Param("authorId") Integer authorId, Pageable pageable);

	List<PostDraft> findDraftsByStationIdAndAuthorId(@Param("stationId") Integer stationId, @Param("authorId") Integer authorId, Pageable pageable);

	List<PostScheduled> findScheduledsByStationIdAndAuthorId(@Param("stationId") Integer stationId, @Param("authorId") Integer authorId, Pageable pageable);

	@Query("select pr.post.id from PostRead pr where pr.person.id=:personId")
	List<Integer> findPostReadByPerson(@Param("personId") Integer personId, Pageable pageable);

	@RestResource(exported = false)
	List<Post> findPostReadByStationAndPerson(@Param("stationId") Integer stationId, @Param("personId") Integer personId, Pageable pageable);

	@RestResource(exported = false)
	List<Post> findPostReadByStationAndPerson(@Param("stationId") Integer stationId, @Param("personId") Integer personId);

	@Query("SELECT post FROM Post post where post.station.id = :stationId ORDER BY post.date DESC")
	List<Post> findPostsOrderByDateDesc(@Param("stationId") Integer stationId, Pageable pageable);

	@RestResource(exported = false)
	@Query("SELECT post FROM Post post where post.station.id = :stationId ORDER BY post.readsCount DESC, post.id DESC")
	List<Post> findPopularPosts(@Param("stationId") Integer stationId, Pageable pageable);

	@RestResource(exported = false)
	@Query("SELECT slug FROM Post")
	Set<String> findSlugs();

	Post findByOriginalPostId(@Param("originalPostId") Integer originalPostId);

	Post findBySlug(@Param("slug") String slug);

	List<Post> findPostBySlug(@Param("slug") String slug);

	@RestResource(exported = false)
	@Query("SELECT post FROM Post post ORDER BY post.date DESC")
	List<Post> findAllPostsOrderByIdDesc();

	@RestResource(exported = false)
	@Query("SELECT wordpressId FROM Post post where post.station.id = :stationId")
	Set<Integer> findWordpressIdsByStation(@Param("stationId") Integer stationId);

	@Query(value = "SELECT *, count(*), sum(readsCount) FROM post, station where station.id = :stationId AND " + "date BETWEEN :dateIni AND :dateEnd group by author_id ORDER BY sum(readsCount) DESC", nativeQuery = true)
	List<Object[]> findPostsOrderByMostReadAuthors(@Param("stationId") Integer stationId, @Param("dateIni") String dateIni, @Param("dateEnd") String dateEnd);

	@Query(value = "SELECT * FROM post, station where station.id = :stationId AND date BETWEEN :dateIni AND :dateEnd ORDER BY favoritesCount DESC, date DESC", nativeQuery = true)
	List<Post> findPostsOrderByFavorites(@Param("stationId") Integer stationId, @Param("dateIni") String dateIni, @Param("dateEnd") String dateEnd);

	@Query(value = "SELECT * FROM post, station where station.id = :stationId AND date BETWEEN :dateIni AND :dateEnd ORDER BY readsCount DESC, date DESC", nativeQuery = true)
	List<Post> findPostsOrderByReads(@Param("stationId") Integer stationId, @Param("dateIni") String dateIni, @Param("dateEnd") String dateEnd);

	@Query(value = "SELECT * FROM post, station where station.id = :stationId AND date BETWEEN :dateIni AND :dateEnd ORDER BY recommendsCount DESC, date DESC", nativeQuery = true)
	List<Post> findPostsOrderByRecommends(@Param("stationId") Integer stationId, @Param("dateIni") String dateIni, @Param("dateEnd") String dateEnd);

	@Query(value = "SELECT * FROM post, station where station.id = :stationId AND date BETWEEN :dateIni AND :dateEnd ORDER BY commentsCount DESC, date DESC", nativeQuery = true)
	List<Post> findPostsOrderByComments(@Param("stationId") Integer stationId, @Param("dateIni") String dateIni, @Param("dateEnd") String dateEnd);

	@RestResource(exported = false)
	@Query("SELECT post FROM Post post where post.author.id = :personId AND post.state = 'PUBLISHED' AND post.station.id in (:stationIds) order by post.id DESC")
	List<Post> findPostByPersonIdAndStations(@Param("personId") Integer personId, @Param("stationIds") List<Integer> stationIds, Pageable pageable);

	@RestResource(exported = false)
	@Query("SELECT post FROM Post post where post.author.id = :personId AND post.state = :state AND post.station.id in (:stationIds) order by post.id DESC")
	List<Post> findPostByPersonIdAndStationsAndState(@Param("personId") Integer personId, @Param("state") String state, @Param("stationIds") List<Integer> stationIds, Pageable pageable);

	@RestResource(exported = false)
	@Query("SELECT post FROM Recommend recommend join recommend.post post where recommend.person.id = :personId AND post.station.id in (:stationIds) order by recommend.id DESC")
	List<Post> findRecommendationsByPersonIdAndStations(Integer personId, List<Integer> stationIds, Pageable pageable);

	@RestResource(exported = false)
	@Query("select (select count(*) from PostRead pr where pr.post.id = p.id), (select count(*) from Comment comment where comment.post.id = p.id), (select count(*) from Recommend recommend where recommend.post.id = p.id) from Post p where p.id = :postId")
	public List<Object[]> findPostStats(@Param("postId") Integer postId);

	@RestResource(exported = false)
	@Query("select post.body from Post post where post.id=:postId")
	public String findPostBodyById(@Param("postId") Integer postId);

	@Query("select person from Person person where person.id = :personId")
	public Person findPersonById(@Param("personId") Integer personId);
}
