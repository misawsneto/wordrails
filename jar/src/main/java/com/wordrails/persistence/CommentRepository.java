package com.wordrails.persistence;

import java.util.Date;
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

	@RestResource(exported = false)
	@Query("select date(comment.date), count(*)  from Comment comment where comment.post.id = :postId and (date(comment.date) >= date(:dateStart) and date(comment.date) <= date(:dateEnd)) group by date(comment.date)")
	public List<Object[]> countByPostAndDate(@Param("postId") Integer postId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Query("select date(comment.date), count(*)  from Comment comment where comment.post.author.id = :authorId and (date(comment.date) >= date(:dateStart) and date(comment.date) <= date(:dateEnd)) group by date(comment.date)")
	List<Object[]> countByAuthorAndDate(@Param("authorId") Integer authorId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
}