package com.wordrails.business;

import com.wordrails.persistence.*;
import com.wordrails.security.PostAndCommentSecurityChecker;
import com.wordrails.util.WordrailsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
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
	private PostRepository postRepository;
	@Autowired
	private PostReadRepository postReadRepository;
	@Autowired
	private CellRepository cellRepository;
	@Autowired
	private CommentRepository commentRepository;
	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private PromotionRepository promotionRepository;
	@Autowired
	private PostAndCommentSecurityChecker postAndCommentSecurityChecker;
	@Autowired
	private BookmarkRepository bookmarkRepository;
	@Autowired
	private RecommendRepository recommendRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Post post) throws UnauthorizedException, NotImplementedException {
		if(post instanceof PostTrash) //post of type Trash is not insertable
			throw new BadRequestException();

		if (postAndCommentSecurityChecker.canWrite(post)) {
			Date now = new Date();
			if (post.date == null) {
				post.date = now;
			}

			if (post.slug == null || post.slug.isEmpty()) {
				String originalSlug = WordrailsUtil.toSlug(post.title);
				post.originalSlug = originalSlug;
				try {
					post.slug = originalSlug;
					postRepository.save(post);
				} catch (org.springframework.dao.DataIntegrityViolationException ex) {
					String hash = WordrailsUtil.generateRandomString(5, "Aa#");
					post.slug = originalSlug + "-" + hash;
				}
			} else {
				post.originalSlug = post.slug;
			}

		} else {
			throw new UnauthorizedException();
		}
	}

	@HandleAfterCreate
	@Transactional
	public void handleAfterCreate(Post post) {
		if (post.state.equals(Post.STATE_SCHEDULED)) {
			postService.schedule(post.id, post.scheduledDate);
		} else if (post.notify && post.state.equals(Post.STATE_PUBLISHED)) {
			postService.buildNotification(post);
		}
	}

	@HandleAfterSave
	@Transactional
	public void handleAfterSave(Post post) {
		if (post.state.equals(Post.STATE_SCHEDULED)) {
			postService.schedule(post.id, post.scheduledDate);
		}
	}

	@HandleBeforeDelete
	@Transactional
	public void handleBeforeDelete(Post post) throws UnauthorizedException {
		if (postAndCommentSecurityChecker.canRemove(post)) {

			List<Image> images = imageRepository.findByPost(post);
			if (images != null && images.size() > 0) {
				postRepository.updateFeaturedImagesToNull(images);
			}
			imageRepository.delete(images);
			cellRepository.delete(cellRepository.findByPost(post));
			commentRepository.delete(post.comments);
			promotionRepository.delete(post.promotions);
			postReadRepository.deleteByPost(post);
			notificationRepository.deleteByPost(post);
			bookmarkRepository.deleteByPost(post);
			recommendRepository.deleteByPost(post);
			postService.removePostIndex(post); // evitando bug de remoção de post que tiveram post alterado.
		} else {
			throw new UnauthorizedException();
		}
	}
}