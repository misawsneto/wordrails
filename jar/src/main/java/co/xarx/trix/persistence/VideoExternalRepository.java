package co.xarx.trix.persistence;

import co.xarx.trix.domain.VideoExternal;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by misael on 4/15/2016.
 */
@RepositoryRestResource(exported = true)
public interface VideoExternalRepository extends DatabaseRepository<VideoExternal, Integer> {
}
