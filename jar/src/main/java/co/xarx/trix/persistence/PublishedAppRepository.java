package co.xarx.trix.persistence;

import co.xarx.trix.domain.PublishedApp;
import co.xarx.trix.util.Constants;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface PublishedAppRepository extends DatabaseRepository<PublishedApp, Integer> {

	@RestResource(exported = false)
	PublishedApp findByTenantIdAndType(@Param("tenantId") String tenantId, @Param("type") Constants.MobilePlatform type);

	@RestResource(exported = false)
	List<PublishedApp> findByTenantId(@Param("tenantId") String tenantId);
}
