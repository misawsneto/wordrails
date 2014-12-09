package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Image;
import com.wordrails.business.Post;

public interface ImageRepository extends JpaRepository<Image, Integer>, QueryDslPredicateExecutor<Image> {
	@RestResource(exported=false)
	List<Image> findByPost(Post post);

	@RestResource(exported=false)
	Image findByFileId(@Param("fileId") Integer fileId);
}