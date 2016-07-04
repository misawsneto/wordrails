package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.ESPost;
import co.xarx.trix.domain.Post;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.NotImplementedException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.AuditService;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.services.post.PostService;
import co.xarx.trix.services.security.PostPermissionService;
import co.xarx.trix.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

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

	@HandleBeforeCreate
	public void handleBeforeCreate(Post post) throws UnauthorizedException, NotImplementedException, BadRequestException {
		Integer stationId = post.getStation().getId();

		boolean canPublish = postPermissionService.canPublishOnStation(stationId);

		if (!canPublish) {
			boolean canCreate = postPermissionService.canCreateOnStation(stationId);

			if (canCreate) {
				post.setState(Post.STATE_UNPUBLISHED);
			} else {
				throw new AccessDeniedException("No permission to create");
			}
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

		if (post.slug == null || post.slug.isEmpty()) {
			post.slug = StringUtil.toSlug(post.title);
		}

		if(post.featuredVideo != null){
			post.imageLandscape = false;
			if (post.featuredImage == null) {
				postService.setVideoFeaturedImage(post);
			}
		}

		if (postRepository.findBySlug(post.slug) != null) {
			post.slug = post.slug + "-" + StringUtil.generateRandomString(6, "aA#");
		}
	}

	@HandleAfterCreate
	public void handleAfterCreate(Post post) {
		notificationCheck(post);
		elasticSearchService.mapThenSave(post, ESPost.class);
	}

	private void notificationCheck(Post post) {
		if (post.state.equals(Post.STATE_PUBLISHED) && post.scheduledDate == null ||
				(post.scheduledDate != null && post.scheduledDate.before(new Date()))
				) {
			if (post.notify && !post.notified)
				postService.sendNewPostNotification(post);
		}
	}

	@HandleAfterSave
	public void handleAfterSave(Post post) {
		notificationCheck(post);
		elasticSearchService.mapThenSave(post, ESPost.class);
		auditService.saveChange(post);
	}

	@HandleBeforeDelete
	@Transactional
	public void handleBeforeDelete(Post post) throws UnauthorizedException {
		cellRepository.delete(cellRepository.findByPost(post));
		commentRepository.delete(post.comments);
		if (post.state.equals(Post.STATE_PUBLISHED)) {
			esPostRepository.delete(post.id); // evitando bug de remoção de post que tiveram post alterado.
		}
	}
}