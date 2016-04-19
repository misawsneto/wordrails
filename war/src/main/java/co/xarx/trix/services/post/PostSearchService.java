package co.xarx.trix.services.post;

import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

public interface PostSearchService {
	List<Post> search(List<Integer> ids, Integer page, Integer size);

	@PostFilter("hasPermission(filterObject, 'co.xarx.trix.domain.Post', 'read')")
	List<Integer> searchIds(PostStatement params);
}
