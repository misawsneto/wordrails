package com.wordrails.persistence;

import com.wordrails.business.Post;
import com.wordrails.business.PostRead;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PostReadRepository extends JpaRepository<PostRead, Integer>, QueryDslPredicateExecutor<PostRead> {

	@Override
	@RestResource(exported = false)
	public <S extends PostRead> S save(S arg0);
	
	public List<PostRead> findPostReadByPersonIdOrderByDate(@Param("personId") Integer personId);
    
    public void deleteByPost(Post post);
}