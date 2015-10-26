package com.wordrails.persistence;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Post;
import com.wordrails.business.Recommend;

public interface RecommendRepository extends JpaRepository<Recommend, Integer>, QueryDslPredicateExecutor<Recommend> {

	List<Recommend> findRecommendsByPersonIdOrderByDate(@Param("personId") Integer personId, Pageable pageable);
	List<Recommend> findRecommendsByPostId(@Param("postId") Integer postId);
	@RestResource(exported=false)
	void deleteByPost(Post post);
	
	@Query("SELECT recommend FROM Recommend recommend WHERE recommend.person.id = :personId AND recommend.post.id = :postId")
	Recommend findRecommendByPersonIdAndPostId(@Param("personId") Integer personId, @Param("postId") Integer postId);
	
	@Query("select r.post.id from Recommend r where r.person.id=:personId)")
	public List<Integer> findRecommendByPerson(@Param("personId") Integer personId, Pageable pageable);

	@RestResource(exported = false)
	@Query("select date(rec.createdAt), count(*)  from Recommend rec where rec.post.id = :postId and (date(rec.createdAt) >= date(:dateStart) and date(rec.createdAt) <= date(:dateEnd)) group by date(rec.createdAt)")
	public List<Object[]> countByPostAndDate(@Param("postId") Integer postId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Query("select date(rec.createdAt), count(*)  from Recommend rec where rec.post.author.id = :authorId and (date(rec.createdAt) >= date(:dateStart) and date(rec.createdAt) <= date(:dateEnd)) group by date(rec.createdAt)")
	List<Object[]> countByAuthorAndDate(@Param("authorId") Integer authorId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);

	@RestResource(exported = false)
	@Query("select date(rec.createdAt), count(*)  from Recommend rec where rec.post.stationId in (select s.id from Station s where s.network.id = :networkId ) and (date(rec.createdAt) >= date(:dateStart) and date(rec.createdAt) <= date(:dateEnd)) group by date(rec.createdAt)")
	List<Object[]> countByNetworkAndDate(@Param("networkId") Integer networkId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
}