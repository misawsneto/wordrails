package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.AuditService;
import co.xarx.trix.services.post.PostModerationService;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.services.security.PostPermissionService;
import co.xarx.trix.util.ImmutablePage;
import co.xarx.trix.util.RestUtil;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2PostsApi;
import com.google.common.collect.Sets;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@NoArgsConstructor
public class V2PostsResource extends AbstractResource implements V2PostsApi {

	private PostModerationService postModerationService;
	private PostPermissionService postPermissionService;
	private PostSearchService postSearchService;
	private PostRepository postRepository;
	private PostConverter postConverter;
	private AuditService auditService;

	@Autowired
	public V2PostsResource(PostModerationService postModerationService, PostPermissionService postPermissionService, PostSearchService postSearchService, PostRepository postRepository, PostConverter postConverter, AuditService auditService){
		this.postModerationService = postModerationService;
		this.postPermissionService = postPermissionService;

		this.postSearchService = postSearchService;
		this.postRepository = postRepository;
		this.postConverter = postConverter;
		this.auditService = auditService;
	}

	@Override
	public Response searchPosts(String query,
								List<Integer> authors,
								List<Integer> stations,
								String state,
								String from,
								String until,
								List<Integer> categories,
								List<String> tags,
								Integer page,
								Integer size,
								List<String> orders,
								List<String> embeds) {

		PostStatement params = new PostStatement(query, authors, stations, state, from, until, categories, tags, orders);

		ImmutablePage<PostData> pageOfData = postSearchService.searchData(params, page, size);

		if (embeds.contains("snippet")) {
			for (PostData post : pageOfData) {
				post.setSnippet(StringUtil.simpleSnippet(post.getBody()));
			}
		}


		Set<String> postEmbeds = Sets.newHashSet("video", "image", "audio", "author", "categories", "body", "snippet");
		super.removeNotEmbeddedData(embeds, pageOfData.items(), PostData.class, postEmbeds);

		return Response.ok().entity(RestUtil.getPageData(pageOfData, orders)).build();
	}

	@Override
	public Response publish(Integer postId) {
		boolean canPublish = postPermissionService.canPublishPost(postId);

		if(canPublish) {
			try {
				postModerationService.publish(postId);
			} catch (IllegalStateException | IllegalArgumentException e) {
				return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
			}
		}

		return Response.ok().build();
	}

	@Override
	public Response unpublish(Integer postId) {
		boolean canPublish = postPermissionService.canPublishPost(postId);

		if (canPublish) {
			try {
				postModerationService.unpublish(postId);
			} catch (IllegalStateException | IllegalArgumentException e) {
				return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
			}
		}

		return Response.ok().build();
	}

	@Override
	public ContentResponse<List<PostView>> findPostsByIds(List<Integer> ids) {
		List<Post> posts = postRepository.findAll(ids);
		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postConverter.convertToViews(posts);
		return response;
	}

	@Override
	public ContentResponse<List<PostData>> getPostVersions(Integer postId) throws NoSuchFieldException, IllegalAccessException {
		ContentResponse response = new ContentResponse();
		response.content = auditService.getPostVersions(postId);
		return response;
	}
}
