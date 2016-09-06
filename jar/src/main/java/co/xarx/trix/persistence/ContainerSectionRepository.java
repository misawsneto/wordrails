package co.xarx.trix.persistence;

import co.xarx.trix.domain.page.ContainerSection;
import co.xarx.trix.domain.page.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface ContainerSectionRepository extends JpaRepository<ContainerSection, Integer>,
		QueryDslPredicateExecutor<ContainerSection>{
}