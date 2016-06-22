package co.xarx.trix.persistence;

import co.xarx.trix.domain.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.List;

@RepositoryRestResource(exported = true)
public interface VideoRepository extends JpaRepository<Video, Integer>, QueryDslPredicateExecutor<Video> {
	@RestResource(exported = true)
	@Query("SELECT video FROM Video video ORDER BY video.id DESC")
	public List<Video> findVideosOrderByDate(Pageable pageable);
}