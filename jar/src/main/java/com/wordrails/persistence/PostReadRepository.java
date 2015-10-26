package com.wordrails.persistence;

import com.wordrails.business.Post;
import com.wordrails.business.PostRead;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PostReadRepository extends JpaRepository<PostRead, Integer>, QueryDslPredicateExecutor<PostRead> {

	@Override
	@RestResource(exported = false)
	public <S extends PostRead> S save(S arg0);
	
	@Query("select postRead from PostRead postRead where (postRead.person.id != 1 OR postRead.person is not null) and postRead.person.id = :personId order by postRead.post.id desc")
	public List<PostRead> findPostReadByPersonIdOrderByDate(@Param("personId") Integer personId);
	
	@Query("select postRead from PostRead postRead where postRead.person.id = :personId order by postRead.post.id desc")
	public List<PostRead> findPostReadByPersonIdOrderByDatePaginated(@Param("personId") Integer personId, Pageable pageable);
    
	@RestResource(exported = false)
    public void deleteByPost(Post post);
	
	@RestResource(exported = false)
	public void findByPostIdAndPersonId(@Param("postId") Integer postId, @Param("personId") Integer personId);
    
    @Query("select count(distinct sessionid) from PostRead pr where pr.createdAt between :dateIni and :dateEnd")
    public Integer countByDistinctSessionid(@Param("dateIni") Date dateIni, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Query("select date(pr.createdAt), count(*)  from PostRead pr where pr.post.id = :postId and (date(pr.createdAt) >= date(:dateStart) and date(pr.createdAt) <= date(:dateEnd)) group by date(pr.createdAt)")
	public List<Object[]> countByPostAndDate(@Param("postId") Integer postId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Query("select date(pr.createdAt), count(*)  from PostRead pr where pr.post.author.id = :authorId and (date(pr.createdAt) >= date(:dateStart) and date(pr.createdAt) <= date(:dateEnd)) group by date(pr.createdAt)")
	List<Object[]> countByAuthorAndDate(@Param("authorId") Integer authorId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);

	@RestResource(exported = false)
	@Modifying
	void deleteByPersonId(Integer id);

	@RestResource(exported = false)
	@Query("select date(pr.createdAt), count(*) from PostRead pr where pr.post.stationId in (select s.id from Station s where s.network.id = :networkId ) and (date(pr.createdAt) >= date(:dateStart) and date(pr.createdAt) <= date(:dateEnd)) group by date(pr.createdAt)")
	List<Object[]> countByNetworkAndDate(@Param("networkId") Integer networkId, @Param("dateStart") Date dateStart, @Param("dateEnd") Date dateEnd);
}

