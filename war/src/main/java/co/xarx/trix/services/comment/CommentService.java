package co.xarx.trix.services.comment;

import co.xarx.trix.api.v2.CommentData;
import co.xarx.trix.domain.Comment;
import co.xarx.trix.eventhandler.CommentEventHandler;
import co.xarx.trix.persistence.CommentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CommentService {

	public CommentRepository commentRepository;
	public CommentEventHandler commentEventHandler;

	@Autowired
	public CommentService(CommentRepository commentRepository, CommentEventHandler commentEventHandler){
		this.commentRepository = commentRepository;
		this.commentEventHandler = commentEventHandler;
	}

	public void deleteComment(Comment comment){
		commentRepository.delete(comment);
		commentEventHandler.handleAfterDelete(comment);
	}

	public void deleteAllComments(List<CommentData> comments) {
		List<Integer> ids = new ArrayList<>();
		for (CommentData comment :
				comments) {
			ids.add(comment.getId());
		}

		List<Comment> allComments = commentRepository.findAll(ids);
		if(allComments != null){
			for (Comment comment :
					allComments) {
				deleteComment(comment);
			}
		}
	}
}
