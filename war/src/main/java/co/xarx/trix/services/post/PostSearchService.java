package co.xarx.trix.services.post;

import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.util.ImmutablePage;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;

public interface PostSearchService {

	Pair searchPosts(String q, Integer personId, Integer page, Integer size, Collection<Integer> postIds);

	Pair searchPosts(String q, Integer personId, Integer page, Integer size, boolean sortByDate);

	ImmutablePage<Post> search(PostStatement params, Integer page, Integer size);

	ImmutablePage<PostData> searchData(PostStatement params, Integer page, Integer size);
}
