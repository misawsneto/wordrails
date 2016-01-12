package co.xarx.trix.aspect;

import co.xarx.trix.domain.Event;
import co.xarx.trix.domain.Loggable;
import co.xarx.trix.persistence.EventRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.services.LogBuilderExecutor;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;

@Aspect
@Component
public class LogAspect {

	@Autowired
	private LogBuilderExecutor logBuilderExecutor;

	@Autowired
	private EventRepository eventRepository;

	@Autowired
	private TrixAuthenticationProvider provider;

	Logger log = Logger.getLogger(this.getClass().getName());

	@AfterReturning("within(org.springframework.data.repository.CrudRepository+) && execution(* *..save(*)) && args(entity)")
	public void logSave(Loggable entity){

		Event event = entity.build(Event.EVENT_SAVE, logBuilderExecutor);

		event.setUserId(provider.getUser().id);
		event.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());

		eventRepository.save(event);
	}

	@AfterReturning("within(co.xarx.trix.persistence.LoggableRepository) && execution(* *..delete(*)) && args(entity)")
	public void logDelete(Loggable entity){

		log.info("---------------------------- Delete ----------------------------");

		Event event = entity.build(Event.EVENT_DELETE, logBuilderExecutor);

		event.setUserId(provider.getUser().id);
		event.setSessionId(RequestContextHolder.currentRequestAttributes().getSessionId());

		eventRepository.save(event);
	}
}
