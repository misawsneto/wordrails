package co.xarx.trix.persistence;

import co.xarx.trix.annotation.SdkInclude;
import co.xarx.trix.domain.DocumentInternal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface DocumentInternalRepository extends DatabaseRepository<DocumentInternal, Integer> {
}