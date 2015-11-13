package com.wordrails.persistence;

import com.wordrails.domain.PostDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface PostDraftRepository extends JpaRepository<PostDraft, Integer>, QueryDslPredicateExecutor<PostDraft> {
}