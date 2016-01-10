package co.xarx.trix.services;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.PostEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {

	@Autowired
	private PostEventRepository postEventRepository;

	@Async
	public void postCreation(Post post){
		postEventRepository.save(new PostEvent(post, Event.EVENT_CREATE));
	}

	@Async
	public void postUpdate(Post post){
		postEventRepository.save(new PostEvent(post, Event.EVENT_UPDATE));
	}

	@Async
	public void postRemoval(Post post){
		postEventRepository.save(new PostEvent(post, Event.EVENT_DELETE));
	}

	@Async
	public void postRead(PostRead read){
		postEventRepository.save(new PostEvent(read));
	}

	@Async
	public void commentCreation(Comment comment){
		postEventRepository.save(new PostEvent(comment, Event.EVENT_COMMENT));
	}

	@Async
	public void commentUpdate(Comment comment){
		postEventRepository.save(new PostEvent(comment, Event.EVENT_UPDATE_COMMENT));
	}

	@Async
	public void commentRemoval(Comment comment){
		postEventRepository.save(new PostEvent(comment, Event.EVENT_REMOVE_COMMENT));
	}

	@Async
	public void recommend(Recommend recommend){
		postEventRepository.save(new PostEvent(recommend, Event.EVENT_RECOMMEND));
	}

	@Async
	public void unrecommend(Recommend recommend){
		postEventRepository.save(new PostEvent(recommend, Event.EVENT_UNRECOMMEND));
	}
}
