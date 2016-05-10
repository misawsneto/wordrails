package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Comment;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.QueryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

@RepositoryEventHandler(Comment.class)
@Component
public class CommentEventHandler {

	private @Autowired QueryPersistence queryPersistence;

	@HandleAfterCreate
	public void handleAfterCreate(Comment comment) throws UnauthorizedException {
		queryPersistence.updateCommentsCount(comment.post.id);
	}

	@HandleAfterSave
	public void handleAfterSave(Comment comment) throws UnauthorizedException {
		queryPersistence.updateCommentsCount(comment.post.id);
	}

	@HandleAfterCreate
	public void handleAfterDelete(Comment comment) throws UnauthorizedException {
		queryPersistence.updateCommentsCount(comment.post.id);
	}
}