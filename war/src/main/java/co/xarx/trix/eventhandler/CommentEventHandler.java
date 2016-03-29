package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Comment;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.QueryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@RepositoryEventHandler(Comment.class)
@Component
public class CommentEventHandler {

	private @Autowired QueryPersistence queryPersistence;

	@HandleBeforeCreate
	public void handleBeforeCreate(Comment comment) throws UnauthorizedException {
		queryPersistence.updateCommentsCount(comment.post.id);
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Comment comment) throws UnauthorizedException {
		queryPersistence.updateCommentsCount(comment.post.id);
	}
}