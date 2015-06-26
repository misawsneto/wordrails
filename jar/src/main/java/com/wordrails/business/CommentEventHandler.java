package com.wordrails.business;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import com.wordrails.persistence.ImageRepository;
import com.wordrails.security.PostAndCommentSecurityChecker;

@RepositoryEventHandler(Comment.class)
@Component
public class CommentEventHandler {
	
	private @Autowired ImageRepository imageRepository;
	private @Autowired PostAndCommentSecurityChecker postAndCommentSecurityChecker;
	
	@HandleBeforeCreate
	public void handleBeforeCreate(Comment comment) throws UnauthorizedException {
		if(!postAndCommentSecurityChecker.canComment(comment)){
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeSave
	public void handleBeforeSave(Comment comment) throws UnauthorizedException {
		if(!postAndCommentSecurityChecker.canEdit(comment)){
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Comment comment) throws UnauthorizedException {
		if(postAndCommentSecurityChecker.canRemove(comment)){
			imageRepository.delete(comment.images);
		}else{
			throw new UnauthorizedException();
		}
	}
}