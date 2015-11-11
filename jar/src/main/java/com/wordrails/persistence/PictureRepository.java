package com.wordrails.persistence;

import com.wordrails.business.File;
import com.wordrails.business.Picture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PictureRepository extends JpaRepository<Picture, Integer>, QueryDslPredicateExecutor<Picture> {

	@RestResource(exported = false)
	@Query("from Picture p where p.file.hash = :hash")
	Picture findByFileHash(@Param("hash") String hash);

	@RestResource(exported = false)
	Picture findByFile(@Param("file") File file);
}