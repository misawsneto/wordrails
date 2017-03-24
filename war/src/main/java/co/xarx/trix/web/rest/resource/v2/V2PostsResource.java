package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.v2.PostData;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.ESPost;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.page.query.statement.PostStatement;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.AuditService;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.services.analytics.RequestWrapper;
import co.xarx.trix.services.analytics.StatEventsService;
import co.xarx.trix.services.post.PostModerationService;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.services.security.PostPermissionService;
import co.xarx.trix.util.ImmutablePage;
import co.xarx.trix.util.SpringDataUtil;
import co.xarx.trix.util.StringUtil;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2PostsApi;
import com.google.common.collect.Sets;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@NoArgsConstructor
public class V2PostsResource extends AbstractResource implements V2PostsApi {

	private AuditService auditService;
	private PostConverter postConverter;
	private PostRepository postRepository;
	private PostSearchService postSearchService;
	private StatEventsService statEventsService;
	private ElasticSearchService elasticSearchService;
	private PostModerationService postModerationService;
	private PostPermissionService postPermissionService;

	@Autowired
	public V2PostsResource(PostModerationService postModerationService, PostPermissionService postPermissionService,
						   PostSearchService postSearchService, PostRepository postRepository, PostConverter
									   postConverter, AuditService auditService, StatEventsService statEventsService, ElasticSearchService
									   elasticSearchService){
		this.postModerationService = postModerationService;
		this.postPermissionService = postPermissionService;
		this.elasticSearchService = elasticSearchService;
		this.statEventsService = statEventsService;

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

		return Response.ok().entity(SpringDataUtil.getPageData(pageOfData, orders)).build();
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
	public Response scheduleUnpublishing(Integer postId, Long timestamp) {
		boolean canUnpublish = postPermissionService.canPublishPost(postId);

		if(timestamp == null || timestamp < 0){
			return Response.status(Response.Status.BAD_GATEWAY).entity("Date must be defined as a valid timestamp").build();
		}

		if(canUnpublish){
			postModerationService.scheduleUnpublishing(postId,  new Date(timestamp));
		} else {
			return Response.status(Response.Status.FORBIDDEN).entity("User cannot unpublish post").build();
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

	@Override
	public Response bulkUpdateStates(BulkUpdateState bulkUpdateState) throws NoSuchFieldException,
			IllegalAccessException {
		if(!(Post.STATE_DRAFT.equals(bulkUpdateState.state) || Post.STATE_PUBLISHED.equals(bulkUpdateState.state) ||
				Post.STATE_TRASH.equals(bulkUpdateState.state) || Post.STATE_NO_AUTHOR.equals(bulkUpdateState.state) ||
				Post.STATE_UNPUBLISHED.equals(bulkUpdateState.state))){
			return  Response.status(400).build();
		}
		List<Post> posts = postRepository.findAll(bulkUpdateState.ids);
		for (Post post : posts) {
			post.state = bulkUpdateState.state;
			postRepository.save(post);
			elasticSearchService.mapThenSave(post, ESPost.class);
		}

		return Response.ok().build();
	}

	@Override
	public Response setPostSeen(Integer postId, Integer timeReading, Long timestamp){
		Post post = postRepository.findOne(postId);
		if(post == null) return Response.status(Response.Status.NOT_FOUND).build();
		if(timestamp == null || timestamp < 0) throw new BadRequestException("A date must be defined as a timestamp");

		RequestWrapper rw = new RequestWrapper(request);
		Date date = timestamp != null ? new Date(timestamp) : new Date();
		statEventsService.newPostreadEvent(post, rw, timeReading, date);
		return Response.ok().build();
	}

	@Override
	public Response postNotify(Integer postId) {
		Post post = postRepository.findOne(postId);
		if(post != null && post.state.equals(Post.STATE_PUBLISHED)){
			post.notified = false;
			post.notify = true;
			postRepository.save(post);
		}

		return Response.ok().build();
	}
}
