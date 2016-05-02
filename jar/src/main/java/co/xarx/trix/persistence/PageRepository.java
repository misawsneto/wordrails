package co.xarx.trix.persistence;

import co.xarx.trix.domain.page.Page;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = true)
public interface PageRepository extends DatabaseRepository<Page, Integer> {

	@Query("select p from Page p where p.station.id = :stationId and p.id = :pageId")
	Page findOne(@Param("stationId") Integer stationId, @Param("pageId") Integer pageId);
}