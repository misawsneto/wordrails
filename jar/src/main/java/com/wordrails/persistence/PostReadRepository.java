package com.wordrails.persistence;

import com.wordrails.business.Post;
import com.wordrails.business.PostRead;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PostReadRepository extends JpaRepository<PostRead, Integer>, QueryDslPredicateExecutor<PostRead> {

	@Override
	@RestResource(exported = false)
	public <S extends PostRead> S save(S arg0);
	
	@Query("select postRead from PostRead postRead where postRead.person.id != 1 and postRead.person.id = :personId order by postRead.post.id desc")
	public List<PostRead> findPostReadByPersonIdOrderByDate(@Param("personId") Integer personId);
	
	@Query("select postRead from PostRead postRead where postRead.person.id = :personId order by postRead.post.id desc")
	public List<PostRead> findPostReadByPersonIdOrderByDatePaginated(@Param("personId") Integer personId, Pageable pageable);
    
	@RestResource(exported = false)
    public void deleteByPost(Post post);
	
	@RestResource(exported = false)
	public void findByPostIdAndPersonId(@Param("postId") Integer postId, @Param("personId") Integer personId);
    
    @Query("select count(distinct sessionid) from PostRead pr where pr.createdAt between :dateIni and :dateEnd")
    public Integer countByDistinctSessionid(@Param("dateIni") Date dateIni, @Param("dateEnd") Date dateEnd);

}