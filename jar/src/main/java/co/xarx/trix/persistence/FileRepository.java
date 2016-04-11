package co.xarx.trix.persistence;

import co.xarx.trix.domain.File;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface FileRepository extends DatabaseRepository<File, Integer> {

	@RestResource(exported = false)
	@Query("select hash from File where id=:id and type='E'")
	String findExternalHashById(@Param("id") Integer id);
}

