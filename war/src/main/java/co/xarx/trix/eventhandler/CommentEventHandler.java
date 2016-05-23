package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Comment;
import co.xarx.trix.domain.ESComment;
import co.xarx.trix.elasticsearch.mapper.ESCommentMapper;
import co.xarx.trix.exception.UnauthorizedException;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.services.ElasticSearchService;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@RepositoryEventHandler(Comment.class)
@Component
public class CommentEventHandler {

	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private ESCommentMapper commentMapper;
	@Autowired
	private ElasticSearchService elasticSearchService;

	@HandleAfterCreate
	public void handleAfterCreate(Comment comment) throws UnauthorizedException {
		queryPersistence.updateCommentsCount(comment.post.id);
		elasticSearchService.mapThenSave(Lists.newArrayList(comment), commentMapper);
	}

	@HandleAfterSave
	public void handleAfterSave(Comment comment) throws UnauthorizedException {
		queryPersistence.updateCommentsCount(comment.post.id);
		elasticSearchService.mapThenSave(Lists.newArrayList(comment), commentMapper);
	}

	@HandleAfterCreate
	public void handleAfterDelete(Comment comment) throws UnauthorizedException {
		queryPersistence.updateCommentsCount(comment.post.id);
		elasticSearchService.deleteEntity(comment.getId()+"", ESComment.class);
	}
}