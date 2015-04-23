package com.wordrails.persistence;

import com.wordrails.business.Image;
import com.wordrails.business.Post;
import com.wordrails.business.Station;
import java.util.List;
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
}
