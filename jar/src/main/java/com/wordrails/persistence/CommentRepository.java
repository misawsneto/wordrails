package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Comment;
import com.wordrails.business.Post;

public interface CommentRepository extends JpaRepository<Comment, Integer>, QueryDslPredicateExecutor<Comment> {
	@RestResource(exported=false)
	List<Comment> findByPost(Post post);

	@Query("SELECT comment FROM Comment comment WHERE comment.post.id = :postId ORDER BY comment.date DESC")
	List<Comment> findPostCommentsOrderByDate(@Param("postId") Integer postId, Pageable pageable);
}