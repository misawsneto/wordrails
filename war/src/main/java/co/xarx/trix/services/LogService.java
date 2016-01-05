package co.xarx.trix.services;

import co.xarx.trix.domain.EventLog;
import co.xarx.trix.domain.Post;
import co.xarx.trix.domain.PostEventLog;
import co.xarx.trix.persistence.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {

	@Autowired
	private EventLogRepository eventLogRepository;

	@Async
	public void logPostCreation(Post post){
		PostEventLog postEventLog = new PostEventLog(post, EventLog.EVENT_CREATE);
		eventLogRepository.save(postEventLog);
	}

//	@Async
	public void logPostUpdate(Post post){
		PostEventLog postEventLog = new PostEventLog(post, EventLog.EVENT_UPDATE);
		eventLogRepository.save(postEventLog);
	}

}
