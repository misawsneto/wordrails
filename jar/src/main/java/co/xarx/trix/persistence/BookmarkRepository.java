package co.xarx.trix.persistence;

import co.xarx.trix.domain.Bookmark;
import co.xarx.trix.domain.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer>, QueryDslPredicateExecutor<Bookmark> {


	@Query("select bookmark from Bookmark bookmark join bookmark.person person where person.id = :personId")
	List<Bookmark> findBookmarksByPersonId(@Param("personId") Integer personId);

	@Query("select bookmark from Bookmark bookmark where bookmark.person.id = :personId order by bookmark.id desc")
	List<Bookmark> findBookmarksByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);

	@Query("select bookmark from Bookmark bookmark join bookmark.post post where post.id = :postId")
	List<Bookmark> findBookmarksByPostId(@Param("postId") Integer postId);

	@RestResource(exported=false)
	void deleteByPost(Post post);

	@Query("SELECT bookmark FROM Bookmark bookmark WHERE bookmark.person.id = :personId")
	List<Bookmark> findBookmarksByPersonId(@Param("personId") Integer personId, Pageable pageable);

	@Query("SELECT bookmark FROM Bookmark bookmark WHERE bookmark.person.id = :personId AND bookmark.post.id = :postId")
	Bookmark findBookmarkByPersonIdAndPostId(@Param("personId") Integer personId, @Param("postId") Integer postId);

	@Query("select b.post.id from Bookmark b where b.person.id=:personId")
	List<Integer> findBookmarkByPerson(@Param("personId") Integer personId, Pageable pageable);

	@RestResource(exported=false)
	@Query("select bookmark from Bookmark bookmark where bookmark.person.id = :personId and bookmark.post.station.id in (:readableIds) order by bookmark.id desc")
	List<Bookmark> findBookmarksByPersonIdOrderByDate(@Param("personId") Integer personId, @Param("readableIds") List<Integer> readableIds, Pageable pageable);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);
}