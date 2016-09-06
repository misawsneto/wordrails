package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkExclude;
import co.xarx.trix.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(exported = true)
public interface CommentRepository extends JpaRepository<Comment, Integer>, QueryDslPredicateExecutor<Comment> {

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Comment findOne(Integer id);

	@Override
	@SdkExclude
	@RestResource(exported = true)
	Comment save(Comment entity);

	@RestResource(exported = true)
	@Query("SELECT comment FROM Comment comment WHERE comment.post.id = :postId ORDER BY comment.date DESC")
	List<Comment> findPostCommentsOrderByDate( @Param("postId")  Integer  postId, Pageable  pageable);

	@RestResource(exported = false)
	@Query("select comment from Comment comment join fetch comment.author where comment.id in (:ids)")
	List<Comment> findAllWithAuthors(@Param("ids") List<Integer> ids, Sort sort);

	@RestResource(exported = false)
	@Query("select count(*) from Comment comment where (comment.post.state = 'PUBLISHED' AND (comment.post.scheduledDate is null OR comment.post.scheduledDate < current_timestamp))")
	Long countPublished();
}