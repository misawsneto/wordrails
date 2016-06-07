package co.xarx.trix.persistence;

import co.xarx.trix.domain.Audio;
import co.xarx.trix.domain.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface AudioRepository extends DatabaseRepository<Audio, Integer> {

	@RestResource(exported = true)
	@Query("SELECT audio FROM Audio audio ORDER BY audio.id DESC")
	public List<Audio> findAudiosOrderByDate(Pageable pageable);
}