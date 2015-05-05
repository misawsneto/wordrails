package com.wordrails.business;

import com.wordrails.GCMService;
import com.wordrails.persistence.CellRepository;
import com.wordrails.persistence.CommentRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.PostReadRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.PromotionRepository;
import com.wordrails.persistence.StationRepository;
import com.wordrails.security.PostAndCommentSecurityChecker;
import com.wordrails.util.WordrailsUtil;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RepositoryEventHandler(Post.class)
@Component
public class PostEventHandler {

	private @Autowired
	PostRepository postRepository;
	@Autowired
	private WordpressService wordpressService;
	private @Autowired
	PostReadRepository postReadRepository;
	private @Autowired
	CellRepository cellRepository;
	private @Autowired
	CommentRepository commentRepository;
	private @Autowired
	ImageRepository imageRepository;
	private @Autowired
	PromotionRepository promotionRepository;
	private @Autowired
	PostAndCommentSecurityChecker postAndCommentSecurityChecker;
	private @Autowired GCMService gcmService;
	
	private @Autowired StationRepository stationRepository;

	@HandleBeforeCreate
	public void handleBeforeCreate(Post post) throws UnauthorizedException, NotImplementedException {
		System.out.println("HANDLE BEFORE CREATE");

		if (postAndCommentSecurityChecker.canWrite(post)) {
			String originalSlug = WordrailsUtil.toSlug(post.title);
			post.originalSlug = originalSlug;
			Date now = new Date();
			if (post.date == null) {
				post.date = now;
			} else if (post.date.after(now)) {
				throw new NotImplementedException("Agendamento de publicações não estão disponíveis.");
			}

			try {
				post.slug = originalSlug;
				postRepository.save(post);
			} catch (org.springframework.dao.DataIntegrityViolationException ex) {
				String hash = WordrailsUtil.generateRandomString(5, "!Aau");
				post.slug = originalSlug + "-" + hash;
			}

		} else {
			throw new UnauthorizedException();
		}
	}
	
	@HandleAfterCreate
	public void handleAfterCreate(Post post){
		buildNotification(post);
	}

	@Async
	@Transactional
	private void buildNotification(Post post){
		
		Notification notification = new Notification();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.station = post.station;
		notification.post = post;
		notification.message = post.title;

		if(post.station != null && post.station.networks != null){
			Station station = stationRepository.findOne(post.station.id);
			for (Network network : station.networks) {
				notification.network = network;
				gcmService.sendToNetwork(network, notification);
			}
		}
	}

	@HandleBeforeSave
	public void handleBeforeSave(Post post) throws UnauthorizedException {
		System.out.println("HANDLE BEFORE SAVE");
		if (postAndCommentSecurityChecker.canEdit(post)) {
			post.lastModificationDate = new Date();
		} else {
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeDelete
	@Transactional
	public void handleBeforeDelete(Post post) throws UnauthorizedException {
		System.out.println("HANDLE BEFORE DELETE");
		if (postAndCommentSecurityChecker.canRemove(post)) {
			Wordpress wp = post.station.wordpress;

			if (wp != null && wp.domain != null && wp.username != null && wp.password != null) {
				WordpressApi api = ServiceGenerator.createService(WordpressApi.class, wp.domain, wp.username, wp.password);
				wordpressService.deletePost(post.wordpressId, api);
			}

			List<Image> images = imageRepository.findByPost(post);
			if (images != null && images.size() > 0) {
				postRepository.updateFeaturedImagesToNull(images);
			}
			imageRepository.delete(images);
			cellRepository.delete(cellRepository.findByPost(post));
			commentRepository.delete(post.comments);
			promotionRepository.delete(post.promotions);
			postReadRepository.deleteByPost(post);
		} else {
			throw new UnauthorizedException();
		}
	}
}
