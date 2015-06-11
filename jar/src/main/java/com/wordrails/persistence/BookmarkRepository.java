package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Bookmark;
import com.wordrails.business.Post;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer>, QueryDslPredicateExecutor<Bookmark> {

//	List<Favorite> findByPersonUsername(@Param("username") String username, Pageable pageable);
	
	List<Bookmark> findBookmarksByPersonId(@Param("personId") Integer personId);
	@Query("select bookmark from Bookmark bookmark join bookmark.person person where person.id = :personId order by bookmark.id desc")
	List<Bookmark> findBookmarksByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);
	List<Bookmark> findBookmarksByPostId(@Param("postId") Integer postId);
	@RestResource(exported=false)
	void deleteByPost(Post post);
	List<Bookmark> findByPersonId(@Param("personId") Integer personId, Pageable pageable);
	void deleteByPostIdAndPersonId(@Param("postId") Integer postId, @Param("personId") Integer personId);
}