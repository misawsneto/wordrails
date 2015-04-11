package com.wordrails.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.wordrails.business.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite, Integer>, QueryDslPredicateExecutor<Favorite> {

}