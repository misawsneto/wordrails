package com.wordrails.persistence;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RestResource;

import com.wordrails.business.Post;
import com.wordrails.business.Promotion;
import com.wordrails.business.Station;

public interface PromotionRepository extends JpaRepository<Promotion, Integer>, QueryDslPredicateExecutor<Promotion> {
	@RestResource(exported=false)
	List<Promotion> findByPost(Post post);

	@RestResource(exported=false)
	List<Promotion> findByStation(Station station);
}