package co.xarx.trix.services.post;

import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.security.access.prepost.PostFilter;

import java.util.Collection;
import java.util.List;

public interface PostSearchService {
	Pair searchPosts(String q, Integer personId, Integer page, Integer size, Collection<Integer> postIds);

	Pair searchPosts(String q, Integer personId, Integer page, Integer size, boolean sortByDate);

	List<Post> search(List<Integer> ids, Integer page, Integer size);

	@PostFilter("hasPermission(filterObject, 'co.xarx.trix.domain.Post', 'read')")
	List<Integer> searchIds(PostStatement params);
}
