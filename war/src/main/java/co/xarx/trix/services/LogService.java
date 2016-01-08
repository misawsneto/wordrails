package co.xarx.trix.services;

import co.xarx.trix.domain.Event;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.PostEvent;
import co.xarx.trix.persistence.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {

	@Autowired
	private EventRepository eventRepository;

	@Async
	public void logCreation(Post post){
		eventRepository.save(new PostEvent(post, Event.EVENT_CREATE).build());
	}

	@Async
	public void logRemoval(Post post){
		eventRepository.save(new PostEvent(post, Event.EVENT_DELETE).build());
	}


}
