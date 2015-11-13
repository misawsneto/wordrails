package com.wordrails.persistence;

import com.wordrails.business.PostDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface PostDraftRepository extends JpaRepository<PostDraft, Integer>, QueryDslPredicateExecutor<PostDraft> {
}