package co.xarx.trix.persistence;

import co.xarx.trix.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Integer>, QueryDslPredicateExecutor<File> {

	@RestResource(exported = false)
	@Query("select hash from File where id=:id and type='E'")
	String findExternalHashById(@Param("id") Integer id);

	@RestResource(exported = false)
	@Query("SELECT f.mime, SUM(f.size) FROM File f WHERE f.tenantId = :tenantId GROUP BY f.mime")
	List<Object[]> sumFilesSizeByMime(@Param("tenantId") String tenantId);

}

