package com.wordrails.persistence;

import com.wordrails.business.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface VideoRepository extends JpaRepository<Video, Integer>, QueryDslPredicateExecutor<Video> {
}
