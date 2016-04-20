package co.xarx.trix.persistence;

import co.xarx.trix.domain.Document;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface DocumentRepository extends DatabaseRepository<Document, Integer> {
}