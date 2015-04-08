package com.wordrails.business;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.wordrails.persistence.CellRepository;
import com.wordrails.persistence.CommentRepository;
import com.wordrails.persistence.ImageRepository;
import com.wordrails.persistence.PostRepository;
import com.wordrails.persistence.PromotionRepository;
import com.wordrails.security.PostAndCommentSecurityChecker;
import com.wordrails.util.Slugfy;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;

@RepositoryEventHandler(Post.class)
@Component
public class PostEventHandler {

    private @Autowired
    PostRepository postRepository;
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
	private @Autowired 
    WordpressService wordpressService;

    @HandleBeforeCreate
    public void handleBeforeCreate(Post post) throws UnauthorizedException, NotImplementedException {        

        if (postAndCommentSecurityChecker.canWrite(post)) {
            String originalSlug = Slugfy.toSlug(post.title);
            post.originalSlug = originalSlug;
            int count = postRepository.countSlugPost(originalSlug);
            if (count > 0) {
                post.slug = originalSlug + "-" + count;
            } else {
                post.slug = originalSlug;
            }
            Date now = new Date();
            if (post.date == null) {
                post.date = now;
            } else if (post.date.after(now)) {
                throw new NotImplementedException("Agendamento de publicações não estão disponíveis.");
            }

        } else {
            throw new UnauthorizedException();
        }
    }

    @HandleBeforeSave
    public void handleBeforeSave(Post post) throws UnauthorizedException {
        if (postAndCommentSecurityChecker.canEdit(post)) {
            post.lastModificationDate = new Date();
        } else {
            throw new UnauthorizedException();
        }
    }
    
    @HandleAfterCreate
    public void handleAfterCreate(Post post) {
        WordpressPost wpPost = wordpressService.createPost(post);
        post.wordpressId = wpPost.id;
        
        postRepository.save(post);
    }
    
    @HandleAfterSave
    public void handleAfterSave(Post post) {
        WordpressPost wpPost = wordpressService.updatePost(post);
    }
    
    @HandleAfterDelete
    public void handleAfterDelete(Post post) {
        wordpressService.deletePost(post);
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
        } else {
            throw new UnauthorizedException();
        }
    }
}
