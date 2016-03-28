package co.xarx.trix.persistence;

import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.PostRead;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Date;
import java.util.List;

public interface PostReadRepository extends DatabaseRepository<PostRead, Integer> {

	@Override
	@RestResource(exported = false)
	<S extends PostRead> S save(S arg0);

	@Query("select postRead from PostRead postRead where " +
			"(postRead.person.id != 1 OR postRead.person is not null) " +
			"and postRead.person.id = :personId order by postRead.post.id desc")
	List<PostRead> findPostReadByPersonIdOrderByDate(@Param("personId") Integer personId);

	@Query("select postRead from PostRead postRead where postRead.person.id = :personId order by postRead.post.id desc")
	List<PostRead> findPostReadByPersonIdOrderByDatePaginated(@Param("personId") Integer personId, Pageable pageable);

	@RestResource(exported = false)
	void deleteByPost(Post post);

	@Query("select count(distinct sessionid) from PostRead pr where pr.createdAt between :dateIni and :dateEnd")
	Integer countByDistinctSessionid(@Param("dateIni") Date dateIni, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Query("select date(pr.createdAt), count(*) from PostRead pr where pr.post.id = :postId and (date(pr.createdAt) >= date(:dateStart) and date(pr.createdAt) <= date(:dateEnd)) group by date(pr.createdAt)")
	List<Object[]> countByPostAndDate(@Param("postId") Integer postId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Query("select date(pr.createdAt), count(*) from PostRead pr where pr.post.author.id = :authorId and (date(pr.createdAt) >= date(:dateStart) and date(pr.createdAt) <= date(:dateEnd)) group by date(pr.createdAt)")
	List<Object[]> countByAuthorAndDate(@Param("authorId") Integer authorId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);

	@RestResource(exported = false)
	@Query("select date(pr.createdAt), count(*) from PostRead pr where pr.post.stationId in (select s.id from Station s) and (date(pr.createdAt) >= date(:dateStart) and date(pr.createdAt) <= date(:dateEnd)) group by date(pr.createdAt)")
	List<Object[]> countByDate(@Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
}

