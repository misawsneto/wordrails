package co.xarx.trix.services;

import co.xarx.trix.domain.EventLog;
import co.xarx.trix.persistence.EventLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class LogService {

	@Autowired
	private EventLogRepository eventLogRepository;

	@Async
	public void logtCreation(EventLog event){
//		PostEventLog postEventLog = new PostEventLog(post, EventLog.EVENT_CREATE);
		eventLogRepository.save(event);
	}

	@Async
	public void logUpdate(EventLog event){
//		PostEventLog postEventLog = new PostEventLog(post, EventLog.EVENT_UPDATE);
		eventLogRepository.save(event);
	}

	@Async
	public void logRemoval(EventLog event){
//		PostEventLog postEventLog = new PostEventLog(post, EventLog.EVENT_UPDATE);
		eventLogRepository.save(event);
//		eventLogRepository.delete();
	}


}
