package co.xarx.trix.persistence;

import co.xarx.trix.domain.DBAndroidApp;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface DBAndroidAppRepository extends DatabaseRepository<DBAndroidApp> {
}