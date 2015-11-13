package co.xarx.trix.persistence;

import co.xarx.trix.domain.PostDraft;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface PostDraftRepository extends JpaRepository<PostDraft, Integer>, QueryDslPredicateExecutor<PostDraft> {
}