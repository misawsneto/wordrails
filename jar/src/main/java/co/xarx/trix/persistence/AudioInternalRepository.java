package co.xarx.trix.persistence;

import co.xarx.trix.domain.AudioInternal;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface AudioInternalRepository extends DatabaseRepository<AudioInternal, Integer> {
}