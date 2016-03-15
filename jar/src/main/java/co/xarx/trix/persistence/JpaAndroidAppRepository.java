package co.xarx.trix.persistence;

import co.xarx.trix.domain.JpaAndroidApp;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface JpaAndroidAppRepository extends DatabaseRepository<JpaAndroidApp, Integer> {
}