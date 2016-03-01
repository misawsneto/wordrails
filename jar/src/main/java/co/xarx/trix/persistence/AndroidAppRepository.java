package co.xarx.trix.persistence;

import co.xarx.trix.domain.AndroidApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface AndroidAppRepository extends JpaRepository<AndroidApp, Integer>, QueryDslPredicateExecutor<AndroidApp> {
}