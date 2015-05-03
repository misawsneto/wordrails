package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.wordrails.business.Favorite;
import com.wordrails.business.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Integer>, QueryDslPredicateExecutor<Recommend> {

//	List<Favorite> findByPersonUsername(@Param("username") String username, Pageable pageable);
	
	List<Favorite> findRecommendsByPersonId(@Param("personId") Integer personId);
	List<Favorite> findRecommendsByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);
	List<Favorite> findRecommendsByPostId(@Param("postId") Integer postId);
}