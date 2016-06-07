package co.xarx.trix.persistence;

import co.xarx.trix.domain.Audio;
import co.xarx.trix.domain.Document;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface DocumentRepository extends DatabaseRepository<Document, Integer> {
	@RestResource(exported = true)
	@Query("SELECT document FROM Document document ORDER BY document.id DESC")
	public List<Document> findDocumentsOrderByDate(Pageable pageable);
}