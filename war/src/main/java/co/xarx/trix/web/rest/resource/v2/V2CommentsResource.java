package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.CommentData;
import co.xarx.trix.domain.page.query.statement.CommentStatement;
import co.xarx.trix.services.comment.CommentSearchService;
import co.xarx.trix.util.RestUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2CommentsApi;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Component
public class V2CommentsResource extends AbstractResource implements V2CommentsApi {

	private CommentSearchService commentSearchService;

	@Autowired
	public V2CommentsResource(CommentSearchService commentSearchService) {
		this.commentSearchService = commentSearchService;
	}

	@Override
	public Response searchComments(String query,
								   List<Integer> authors,
								   List<Integer> posts,
								   List<Integer> stations,
								   String from,
								   String until,
								   Integer page,
								   Integer size,
								   List<String> orders,
								   List<String> embeds) {

		CommentStatement params = new CommentStatement(query, authors, posts, stations, from, until, orders);

		Sort sort = RestUtil.getSort(params.getOrders());

		List<CommentData> data = commentSearchService.search(params, page, size, sort);

		Set<String> allEmbeds = Sets.newHashSet("author");

		super.removeNotEmbeddedData(embeds, data, CommentData.class, allEmbeds);

		Pageable pageable = RestUtil.getPageable(page, size, orders);
		Page p = new PageImpl(data, pageable, data.size());

		return Response.ok().entity(p).build();
	}
}
