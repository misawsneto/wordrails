package co.xarx.trix.persistence;

import co.xarx.trix.domain.page.Page;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface PageRepository extends DatabaseRepository<Page, Integer> {
}