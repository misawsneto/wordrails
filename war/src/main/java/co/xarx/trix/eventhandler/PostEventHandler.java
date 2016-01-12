package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.PostTrash;
import co.xarx.trix.elasticsearch.domain.ESPost;
import co.xarx.trix.elasticsearch.repository.ESPostRepository;
import co.xarx.trix.exception.BadRequestException;
import co.xarx.trix.exception.NotImplementedException;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.security.PostAndCommentSecurityChecker;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.SchedulerService;
import co.xarx.trix.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Set;

@RepositoryEventHandler(Post.class)
@Component
public class PostEventHandler {

	@Autowired
	private MobileService mobileService;
	@Autowired
	private SchedulerService schedulerService;
	@Autowired
	private PostReadRepository postReadRepository;
	@Autowired
	private CellRepository cellRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private PostAndCommentSecurityChecker postAndCommentSecurityChecker;
	@Autowired
	private RecommendRepository recommendRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private ESPostRepository esPostRepository;

	@HandleBeforeCreate
	public void handleBeforeCreate(Post post) throws UnauthorizedException, NotImplementedException, BadRequestException {
		if(post instanceof PostTrash) //post of type Trash is not insertable
			throw new BadRequestException();

		if (postAndCommentSecurityChecker.canWrite(post)) {
			savePost(post);
		} else {
			throw new UnauthorizedException();
		}
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
			if (post.notify) {
				mobileService.buildNotification(post);
			}

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

	@HandleAfterDelete
	@Transactional
	public void handleAfterDelete(Post post){
		System.out.println("------------------- After delete --------------------");
	}

	@HandleBeforeDelete
	@Transactional
	public void handleBeforeDelete(Post post) throws UnauthorizedException {
		System.out.println("------------------- Before delete --------------------");
		if (postAndCommentSecurityChecker.canRemove(post)) {

			cellRepository.delete(cellRepository.findByPost(post));
			commentRepository.delete(post.comments);
			postReadRepository.deleteByPost(post);
			notificationRepository.deleteByPost(post);
			recommendRepository.deleteByPost(post);
			if (post.state.equals(Post.STATE_PUBLISHED)) {
				elasticSearchService.deleteIndex(post.id, esPostRepository); // evitando bug de remoção de post que tiveram post alterado.
			}
		} else {
			throw new UnauthorizedException();
		}
	}
}