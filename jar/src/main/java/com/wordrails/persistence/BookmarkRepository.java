package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import com.wordrails.business.Bookmark;
import com.wordrails.business.Favorite;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer>, QueryDslPredicateExecutor<Bookmark> {

//	List<Favorite> findByPersonUsername(@Param("username") String username, Pageable pageable);
	
	List<Favorite> findBookmarksByPersonId(@Param("personId") Integer personId);
	List<Favorite> findBookmarksByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);
	List<Favorite> findBookmarksByPostId(@Param("postId") Integer postId);
}