package co.xarx.trix.persistence;

import co.xarx.trix.domain.Video;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface VideoRepository extends DatabaseRepository<Video, Integer> {
}