package co.xarx.trix.persistence;

import co.xarx.trix.domain.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;

@RepositoryRestResource()
public interface CommentRepository extends TrixRepository<Comment> {

	@Query("SELECT comment FROM Comment comment WHERE comment.post.id = :postId ORDER BY comment.date DESC")
	List<Comment> findPostCommentsOrderByDate(@Param("postId") Integer postId, Pageable pageable);

	@RestResource(exported = false)
	@Query("select date(comment.date), count(*)  from Comment comment where comment.post.id = :postId and (date(comment.date) >= date(:dateStart) and date(comment.date) <= date(:dateEnd)) group by date(comment.date)")
	List<Object[]> countByPostAndDate(@Param("postId") Integer postId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Query("select date(comment.date), count(*)  from Comment comment where comment.post.author.id = :authorId and (date(comment.date) >= date(:dateStart) and date(comment.date) <= date(:dateEnd)) group by date(comment.date)")
	List<Object[]> countByAuthorAndDate(@Param("authorId") Integer authorId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Query("select date(comment.date), count(*)  from Comment comment where comment.post.stationId in (select s.id from Station s) and (date(comment.date) >= date(:dateStart) and date(comment.date) <= date(:dateEnd)) group by date(comment.date)")
	List<Object[]> countByDate(@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
}