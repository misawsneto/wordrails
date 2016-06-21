package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.v2.CommentData;
import co.xarx.trix.domain.page.query.statement.CommentStatement;
import co.xarx.trix.services.comment.CommentSearchService;
import co.xarx.trix.services.comment.CommentService;
import co.xarx.trix.util.ImmutablePage;
import co.xarx.trix.util.RestUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2CommentsApi;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public class V2CommentsResource extends AbstractResource implements V2CommentsApi {

	private CommentSearchService commentSearchService;
	private CommentService commentService;

	@Autowired
	public V2CommentsResource(CommentSearchService commentSearchService, CommentService commentService) {
		this.commentSearchService = commentSearchService;
		this.commentService = commentService;
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

		Sort sort = RestUtil.getSort(orders);
		CommentStatement params = new CommentStatement(query, authors, posts, stations, from, until, orders);

		ImmutablePage<CommentData> pageOfData = commentSearchService.search(params, page, size, sort);


		Set<String> allEmbeds = Sets.newHashSet("author");
		super.removeNotEmbeddedData(embeds, pageOfData.items(), CommentData.class, allEmbeds);

		return Response.ok().entity(RestUtil.getPageData(pageOfData, orders)).build();
	}

	@DELETE
	@Path("")
	public void deleteComment(List<CommentData> comments) throws IOException{
		commentService.deleteAllComments(comments);
	}
}
