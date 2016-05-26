package co.xarx.trix.actors;

import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class TaskActor extends UntypedActor {

	private final LoggingAdapter logger = Logging.getLogger(getContext().system(), "TaskActor");

	@Override
	public void preStart() throws Exception {

		logger.info("TaskActor Starting up");

		super.preStart();
	}

    private final LoggingAdapter log = Logging
        .getLogger(getContext().system(), "TaskProcessor");


    @Override
    public void onReceive(Object message) throws Exception {
        log.debug("Task actor");
    }
}