package co.xarx.trix.persistence.custom;

import co.xarx.trix.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

	void delete(Integer id);

	Page<Post> findAll(Pageable pageable);
}
