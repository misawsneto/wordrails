package co.xarx.trix.persistence;

import co.xarx.trix.domain.Audio;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface AudioRepository extends JpaRepository<Audio, Integer>, QueryDslPredicateExecutor<Audio> {

	@RestResource(exported = true)
	@Query("SELECT audio FROM Audio audio ORDER BY audio.id DESC")
	public List<Audio> findAudiosOrderByDate(Pageable pageable);
}