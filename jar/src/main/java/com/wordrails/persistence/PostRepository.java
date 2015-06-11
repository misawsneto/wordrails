package com.wordrails.persistence;

import com.wordrails.business.Image;
import com.wordrails.business.Post;
import com.wordrails.business.Station;
import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PostRepository extends JpaRepository<Post, Integer>, QueryDslPredicateExecutor<Post> {
	List<Post> findPostsFromOrPromotedToStation(@Param("stationId") int stationId, Pageable pageable);
	List<Post> findPosts(@Param("stationId") Integer stationId, @Param("termId") Integer termId, Pageable pageable);
	List<Post> findPostsAndPostsPromoted(@Param("stationId") Integer stationId, @Param("termsIds") List<Integer> termsIds, Pageable pageable);
	List<Post> findPostsNotPositioned(@Param("stationId") Integer stationId, @Param("termsIds") List<Integer> termsIds, 
			@Param("idsToExclude") List<Integer> idsToExclude, Pageable pageable);

	@RestResource(exported=false)
    List<Post> findByFeaturedImages(@Param("featuredImages") List<Image> featuredImage);
    @RestResource(exported = false)
    List<Post> findByStation(Station station);
    @RestResource(exported = false)
    Post findByWordpressId(Integer wordpressId);
	@RestResource(exported=false)
	List<Post> findPostsAndPostsPromotedByBody(@Param("stationId") Integer stationId, @Param("body") String body, Pageable pageable);
	@RestResource(exported=false)
	List<Post> findPostsAndPostsPromotedByTermId(@Param("stationId") Integer stationId, @Param("termId") Integer termId, Pageable pageable);
	@RestResource(exported=false)
	List<Post> findPostsAndPostsPromotedByAuthorId(@Param("stationId") Integer stationId, @Param("authorId") Integer authorId, Pageable pageable);
	@RestResource(exported=false)
	int countSlugPost(@Param("slug")String slug);
	
	@RestResource(exported=false)
	@Modifying
	@Query(value="UPDATE Post post SET post.featuredImage = null WHERE post.featuredImage IN (:featuredImages)")
	void updateFeaturedImagesToNull(@Param("featuredImages") List<Image> featuredImage);
	
	@RestResource(exported=false)
	List<Post> findPostsByStationIdAndAuthorIdAndState(@Param("stationId") Integer stationId, @Param("authorId") Integer authorId, @Param("state") String state, Pageable pageable);
	
	@RestResource(exported=false)
	public List<Post> findUnreadByStationAndPerson(@Param("stationId") Integer stationId, @Param("personId") Integer personId, Pageable pageable);
	public List<Post> findUnreadByStationAndPerson(@Param("stationId") Integer stationId, @Param("personId") Integer personId);
	
	@Query("SELECT post FROM Post post where post.station.id = :stationId ORDER BY post.id DESC")
	public List<Post> findPostsOrderByDateDesc(@Param("stationId") Integer stationId, Pageable pageable);
	
	@Query("SELECT post FROM Post post where post.station.id = :stationId ORDER BY post.readsCount DESC, post.id DESC")
	public List<Post> findPopularPosts(@Param("stationId") Integer stationId, Pageable pageable);
    
	@RestResource(exported=false)
    @Query("SELECT slug FROM Post")
    public Set<String> findSlugs();
	
	public Post findBySlug(@Param("slug") String slug);
	
	@RestResource(exported=false)
	@Query("SELECT post FROM Post post ORDER BY post.id DESC")
	public List<Post> findAllPostsOrderByIdDesc();
	
	@RestResource(exported=false)
    @Query("SELECT wordpressId FROM Post post where post.station.id = :stationId")
    public Set<Integer> findWordpressIdsByStation(@Param("stationId") Integer stationId);
    
	@Query(value = "SELECT *, count(*), sum(readsCount) FROM post, station where station.id = ?1 AND " +
        "date BETWEEN ?2 AND ?3 group by author_id ORDER BY sum(readsCount) DESC", nativeQuery = true)
	public List<Object[]> findPostsOrderByMostReadAuthors(@Param("stationId") Integer stationId, @Param("dateIni") String dateIni, @Param("dateEnd") String dateEnd);
	
	@Query(value = "SELECT * FROM post, station where station.id = 2 AND date BETWEEN ?2 AND ?3 ORDER BY favoritesCount DESC, date DESC", nativeQuery = true)
	public List<Post> findPostsOrderByFavorites(@Param("stationId") Integer stationId, @Param("dateIni") String dateIni, @Param("dateEnd") String dateEnd);
	
	@Query(value = "SELECT * FROM post, station where station.id = 2 AND date BETWEEN ?2 AND ?3 ORDER BY readsCount DESC, date DESC", nativeQuery = true)
	public List<Post> findPostsOrderByReads(@Param("stationId") Integer stationId, @Param("dateIni") String dateIni, @Param("dateEnd") String dateEnd);
	
	@Query(value = "SELECT * FROM post, station where station.id = 2 AND date BETWEEN ?2 AND ?3 ORDER BY recommendsCount DESC, date DESC", nativeQuery = true)
	public List<Post> findPostsOrderByRecommends(@Param("stationId") Integer stationId, @Param("dateIni") String dateIni, @Param("dateEnd") String dateEnd);
	
	@Query(value = "SELECT * FROM post, station where station.id = 2 AND date BETWEEN ?2 AND ?3 ORDER BY commentsCount DESC, date DESC", nativeQuery = true)
	public List<Post> findPostsOrderByComments(@Param("stationId") Integer stationId, @Param("dateIni") String dateIni, @Param("dateEnd") String dateEnd);
	
	@RestResource(exported=false)
	@Query("SELECT post FROM Post post where post.author.id = :personId AND post.state = 'PUBLISHED' AND post.station.id in (:stationIds) order by post.id DESC")
	public List<Post> findPostByPersonIdAndStations(@Param("personId") Integer personId, @Param("stationIds") List<Integer> stationIds, Pageable pageable);
	
	@RestResource(exported=false)
	@Query("SELECT post FROM Recommend recommend join recommend.post post where recommend.person.id = :personId AND post.state = 'PUBLISHED' AND post.station.id in (:stationIds) order by recommend.id DESC")
	public List<Post> findRecommendationsByPersonIdAndStations(Integer personId,
			List<Integer> stationIds, Pageable pageable);
}
