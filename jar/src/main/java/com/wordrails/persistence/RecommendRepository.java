package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Post;
import com.wordrails.business.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Integer>, QueryDslPredicateExecutor<Recommend> {

//	List<Favorite> findByPersonUsername(@Param("username") String username, Pageable pageable);
	
	List<Recommend> findRecommendsByPersonId(@Param("personId") Integer personId);
	List<Recommend> findRecommendsByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);
	List<Recommend> findRecommendsByPostId(@Param("postId") Integer postId);
	@RestResource(exported=false)
	void deleteByPost(Post post);
	
	@Query("SELECT recommend FROM Recommend recommend WHERE recommend.person.id = :personId AND recommend.post.id = :postId")
	Recommend findRecommendByPersonIdAndPostId(@Param("personId") Integer personId, @Param("postId") Integer postId);
}