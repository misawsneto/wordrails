package co.xarx.trix.persistence;

import co.xarx.trix.domain.FileContents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface FileContentsRepository extends JpaRepository<FileContents, Integer>, QueryDslPredicateExecutor<FileContents> {
}