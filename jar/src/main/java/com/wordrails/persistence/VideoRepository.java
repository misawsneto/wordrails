package com.wordrails.persistence;

import com.wordrails.domain.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface VideoRepository extends JpaRepository<Video, Integer>, QueryDslPredicateExecutor<Video> {
}
