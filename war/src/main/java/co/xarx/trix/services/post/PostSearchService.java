package co.xarx.trix.services.post;

import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.List;

public interface PostSearchService {

	Pair searchPosts(String q, Integer personId, Integer page, Integer size, Collection<Integer> postIds);

	Pair searchPosts(String q, Integer personId, Integer page, Integer size, boolean sortByDate);

	List<Post> search(PostStatement params, Integer page, Integer size);

	List<PostData> searchData(PostStatement params, Integer page, Integer size);
}
