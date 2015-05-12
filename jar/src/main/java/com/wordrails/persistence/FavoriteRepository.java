package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Favorite;
import com.wordrails.business.Post;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer>, QueryDslPredicateExecutor<Favorite> {

//	List<Favorite> findByPersonUsername(@Param("username") String username, Pageable pageable);
	
	List<Favorite> findFavoritesByPersonId(@Param("personId") Integer personId);
	List<Favorite> findFavoritesByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);
	List<Favorite> findFavoritesByPostId(@Param("postId") Integer postId);
	@RestResource(exported = false)
	void deleteByPost(Post post);
}