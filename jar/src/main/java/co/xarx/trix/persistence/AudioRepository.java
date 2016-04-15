package co.xarx.trix.persistence;

import co.xarx.trix.domain.Audio;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface AudioRepository extends DatabaseRepository<Audio, Integer> {
}