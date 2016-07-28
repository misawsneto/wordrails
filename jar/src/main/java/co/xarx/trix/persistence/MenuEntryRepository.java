package co.xarx.trix.persistence;

import co.xarx.trix.domain.MenuEntry;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface MenuEntryRepository extends DatabaseRepository<MenuEntry, Integer> {
}