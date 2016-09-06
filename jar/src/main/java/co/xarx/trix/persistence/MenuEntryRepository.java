package co.xarx.trix.persistence;

import co.xarx.trix.domain.MenuEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface MenuEntryRepository extends JpaRepository<MenuEntry, Integer>, QueryDslPredicateExecutor<MenuEntry> {
}