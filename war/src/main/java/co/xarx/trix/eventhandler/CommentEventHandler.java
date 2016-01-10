package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Comment;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.security.PostAndCommentSecurityChecker;
import co.xarx.trix.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

@RepositoryEventHandler(Comment.class)
@Component
public class CommentEventHandler {

	private @Autowired PostAndCommentSecurityChecker postAndCommentSecurityChecker;
	private @Autowired QueryPersistence queryPersistence;
	private @Autowired LogService logService;

	@HandleBeforeCreate
	public void handleBeforeCreate(Comment comment) throws UnauthorizedException {
		if(!postAndCommentSecurityChecker.canComment(comment)){
			throw new UnauthorizedException();
		}
		queryPersistence.updateCommentsCount(comment.post.id);
	}

	@HandleAfterCreate
	public void handleAfterCreate(Comment comment){
		logService.commentCreation(comment);
	}

	@HandleAfterSave
	public void handleAfterSave(Comment comment){
		logService.commentUpdate(comment);
	}

	@HandleBeforeSave
	public void handleBeforeSave(Comment comment) throws UnauthorizedException {
		if(!postAndCommentSecurityChecker.canEdit(comment)){
			throw new UnauthorizedException();
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Comment comment) throws UnauthorizedException {
		queryPersistence.updateCommentsCount(comment.post.id);
	}

	@HandleAfterDelete
	public void handleAfterDelete(Comment comment){
		logService.commentRemoval(comment);
	}
}