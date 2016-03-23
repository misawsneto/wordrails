package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.ESPost;
import co.xarx.trix.domain.Post;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.NotImplementedException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.ESStartupIndexerService;
import co.xarx.trix.services.SchedulerService;
import co.xarx.trix.services.post.PostService;
import co.xarx.trix.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.core.annotation.*;
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
	private ESStartupIndexerService elasticSearchService;
	@Autowired
	private ESPostRepository esPostRepository;

	@HandleBeforeCreate
	public void handleBeforeCreate(Post post) throws UnauthorizedException, NotImplementedException, BadRequestException {
		savePost(post);
	}

	public void savePost(Post post) {
		Date now = new Date();
		if (post.date == null) {
			post.date = now;
		}

		if (post.slug == null || post.slug.isEmpty()) {
			String originalSlug = StringUtil.toSlug(post.title);
			try {
				post.slug = originalSlug + "-" + StringUtil.generateRandomString(8, "A#").toLowerCase();
			} catch (DataIntegrityViolationException ex) {
				post.slug = originalSlug + "-" + StringUtil.generateRandomString(8, "A#").toLowerCase();
			}
		} else {
			post.originalSlug = post.slug;
		}
	}

	@HandleAfterCreate
	public void handleAfterCreate(Post post) {
		if (post.state.equals(Post.STATE_SCHEDULED)) {
			schedulerService.schedule(post.id, post.scheduledDate);
		} else if (post.state.equals(Post.STATE_PUBLISHED)) {
			if (post.notify)
				postService.sendNewPostNotification(post);

			elasticSearchService.saveIndex(post, ESPost.class, esPostRepository);
		}
	}

	@HandleAfterSave
	public void handleAfterSave(Post post) {
		if (post.state.equals(Post.STATE_SCHEDULED)) {
			schedulerService.schedule(post.id, post.scheduledDate);
		} else if (post.state.equals(Post.STATE_PUBLISHED)) {
			elasticSearchService.saveIndex(post, ESPost.class, esPostRepository);
		}
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