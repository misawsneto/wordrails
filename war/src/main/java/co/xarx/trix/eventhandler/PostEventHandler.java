package co.xarx.trix.eventhandler;

import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.ESPost;
import co.xarx.trix.domain.Post;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.ConflictException;
import co.xarx.trix.exception.NotImplementedException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.AuditService;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.services.NotificationTargetService;
import co.xarx.trix.services.post.PostService;
import co.xarx.trix.services.security.PostPermissionService;
import co.xarx.trix.util.StringUtil;
import org.jcodec.common.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@RepositoryEventHandler(Post.class)
@Component
public class PostEventHandler {

	@Autowired
	private PostService postService;
	@Autowired
	private CellRepository cellRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private ESPostRepository esPostRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostPermissionService postPermissionService;
	@Autowired
	private AuditService auditService;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private NotificationTargetService targetService;

	@HandleBeforeCreate
	public void handleBeforeCreate(Post post) throws UnauthorizedException, NotImplementedException, BadRequestException, ConflictException {
		Integer stationId = post.getStation().getId();


		Pageable page = new PageRequest(0, 1, Sort.Direction.DESC, "id");
		List<Post> postPage = postRepository.findLatestPosts(TenantContextHolder.getCurrentTenantId(), page);
		if(postPage != null && postPage.size() > 0 && postPage.get(0).title.equals(post.title) && postPage.get(0)
				.station.id
				.equals(post
				.station.id))
			throw new ConflictException("Title conflict: " + post.title);

		boolean canPublish = postPermissionService.canPublishOnStation(stationId);

		if (!canPublish) {
			boolean canCreate = postPermissionService.canCreateOnStation(stationId);

			if (canCreate) {
				if(post.state != null) {
					if (!post.state.equals(Post.STATE_UNPUBLISHED) && !post.state.equals(Post
							.STATE_DRAFT))
						throw new AccessDeniedException("No permission to use state: " + post.state);
				}else{
					post.setState(Post.STATE_UNPUBLISHED);
				}
			} else {
				throw new AccessDeniedException("No permission to create");
			}
		}

		if (post.slug == null || post.slug.isEmpty()) {
			post.slug = StringUtil.toSlug(post.title);
		}

		List<Post> posts = queryPersistence.findPostBySlug(post.slug, TenantContextHolder.getCurrentTenantId());
		if (posts != null && posts.size() > 0) {
			post.slug = post.slug + "-" + StringUtil.generateRandomString(6, "aA#");
		}

		handleBeforeSave(post);
	}

	@HandleBeforeSave
	public void handleBeforeSave(Post post) {
		if (post.date == null) {
			if(post.scheduledDate == null)
				post.date = new Date();
			else
				post.date = new Date(post.scheduledDate.getTime());
		}else{
			if (post.scheduledDate != null && post.date.after(post.scheduledDate))
				post.date = new Date(post.scheduledDate.getTime());
		}

		if(post.featuredVideo != null){
			post.imageLandscape = false;
			if (post.featuredImage == null) {
				postService.setVideoFeaturedImage(post);
			}
		}

		if (post.slug == null || post.slug.isEmpty()) {
			post.slug = StringUtil.toSlug(post.title);
			List<Post> posts = queryPersistence.findPostBySlug(post.slug, TenantContextHolder.getCurrentTenantId());
			if (posts != null && posts.size() > 0) {
				post.slug = post.slug + "-" + StringUtil.generateRandomString(6, "aA#");
			}
		}
	}

	@HandleAfterCreate
	public void handleAfterCreate(Post post) {
		elasticSearchService.mapThenSave(post, ESPost.class);
		notificationCheck(post);
		targetService.postCheckTarget(post);
	}

	public void notificationCheck(Post post) {
		if (post.state.equals(Post.STATE_PUBLISHED) && post.notify && !post.notified
				&& (post.scheduledDate == null || (post.scheduledDate != null && post.scheduledDate.before(new Date())) )
				) {
			Logger.info("Start sending notifications");
			postService.asyncSendNewPostNotification(post, TenantContextHolder.getCurrentTenantId());
			post.notified = true;
			postRepository.save(post);
			Logger.info("Continue saving post after creating notifications");
		}
	}

	@HandleAfterSave
	public void handleAfterSave(Post post) {
		notificationCheck(post);
		targetService.postCheckTarget(post);
		auditService.saveChange(post);
		elasticSearchService.mapThenSave(post, ESPost.class);
	}

	@HandleBeforeDelete
	@Transactional
	public void handleBeforeDelete(Post post) throws UnauthorizedException {
		post.tags.clear();
		postRepository.save(post);
		cellRepository.delete(cellRepository.findByPost(post));
		commentRepository.delete(post.comments);
		if (post.state.equals(Post.STATE_PUBLISHED)) {
			esPostRepository.delete(post.id); // evitando bug de remoção de post que tiveram post alterado.
		}
	}
}