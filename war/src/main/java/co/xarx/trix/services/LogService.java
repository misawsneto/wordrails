package co.xarx.trix.services;

import co.xarx.trix.domain.Event;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.PostEvent;
import co.xarx.trix.domain.PostRead;
import co.xarx.trix.persistence.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {

	@Autowired
	private EventRepository eventRepository;

	@Async
	public void logPostCreation(Post post){
		eventRepository.save(new PostEvent(post, Event.EVENT_CREATE));
	}

	@Async
	public void logPostUpdate(Post post){
		eventRepository.save(new PostEvent(post, Event.EVENT_UPDATE));
	}

	@Async
	public void logPostRemoval(Post post){
		eventRepository.save(new PostEvent(post, Event.EVENT_DELETE));
	}

	public void logPostRead(PostRead read){
		eventRepository.save(new PostEvent(read));
	}

}
