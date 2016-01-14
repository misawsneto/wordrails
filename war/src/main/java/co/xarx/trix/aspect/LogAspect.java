package co.xarx.trix.aspect;

import co.xarx.trix.domain.Loggable;
import co.xarx.trix.domain.event.Event;
import co.xarx.trix.persistence.EventRepository;
import co.xarx.trix.services.LogBuilderExecutor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LogAspect {

	@Autowired
	private LogBuilderExecutor logBuilderExecutor;

	@Autowired
	private EventRepository eventRepository;

	@AfterReturning("within(org.springframework.data.repository.CrudRepository+) && execution(* *..save(*)) && args(entity)")
	public void logSave(Loggable entity){

		Event event = entity.build(Event.EVENT_SAVE, logBuilderExecutor);

		eventRepository.save(event);
	}
}
