package co.xarx.trix.eventhandler;

import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.ESPost;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.Station;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.NotImplementedException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.services.SchedulerService;
import co.xarx.trix.services.post.PostService;
import co.xarx.trix.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.acls.model.Permission;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@RepositoryEventHandler(Post.class)
@Component
public class PostEventHandler {

	@Autowired
	private PostService postService;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private PostReadRepository postReadRepository;
	@Autowired
	private CellRepository cellRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private ESPostRepository esPostRepository;
	@Autowired
	private PermissionEvaluator permissionEvaluator;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private StationRepository stationRepository;

	@HandleBeforeCreate
	public void handleBeforeCreate(Post post) throws UnauthorizedException, NotImplementedException, BadRequestException {
		Integer stationId = post.getStation().getId();

		boolean hasPermissionToCreate = permissionEvaluator.hasPermission(SecurityContextHolder.getContext()
				.getAuthentication(), stationId, Station
				.class.getName(), new Permission[]{Permissions.CREATE, Permissions.ADMINISTRATION});

		if(!hasPermissionToCreate) {
			boolean hasPermissionToRead = permissionEvaluator.hasPermission(SecurityContextHolder.getContext()
					.getAuthentication(), stationId, Station
					.class.getName(), Permissions.READ);
			Station station = stationRepository.findOne(stationId);
			boolean canWrite = station.isWritable() && hasPermissionToRead;
			if(!canWrite)
				throw new AccessDeniedException("No permission to create");
		}

		savePost(post);
	}

	public void savePost(Post post) {
		if (post.date == null) {
			post.date = new Date();
		}

		if (post.slug == null || post.slug.isEmpty()) {
			post.slug = StringUtil.toSlug(post.title);
		}

		if(postRepository.findBySlug(post.slug) != null){
			post.slug = post.slug + "-" + StringUtil.generateRandomString(6, "aA#");
		}
	}

	@HandleAfterCreate
	public void handleAfterCreate(Post post) {
		if (post.state.equals(Post.STATE_SCHEDULED)) {
			schedulerService.schedule(post.id, post.scheduledDate);
		} else if (post.state.equals(Post.STATE_PUBLISHED)) {
			if (post.notify)
				postService.sendNewPostNotification(post);

		}
		elasticSearchService.mapThenSave(post, ESPost.class);
	}

	@HandleAfterSave
	public void handleAfterSave(Post post) {
		if (post.state.equals(Post.STATE_SCHEDULED)) {
			schedulerService.schedule(post.id, post.scheduledDate);
		} else if (post.state.equals(Post.STATE_PUBLISHED)) {
		}
		elasticSearchService.mapThenSave(post, ESPost.class);
	}

	@HandleBeforeDelete
	@Transactional
	public void handleBeforeDelete(Post post) throws UnauthorizedException {
		cellRepository.delete(cellRepository.findByPost(post));
		commentRepository.delete(post.comments);
		postReadRepository.deleteByPost(post);
		notificationRepository.deleteByPost(post);
		if (post.state.equals(Post.STATE_PUBLISHED)) {
			esPostRepository.delete(post.id); // evitando bug de remoção de post que tiveram post alterado.
		}
	}
}