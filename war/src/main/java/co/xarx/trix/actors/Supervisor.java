package co.xarx.trix.actors;

import akka.actor.ActorRef;
import akka.actor.Terminated;
import akka.actor.UntypedActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import akka.routing.ActorRefRoutee;
import akka.routing.Routee;
import akka.routing.Router;
import akka.routing.SmallestMailboxRoutingLogic;
import co.xarx.trix.actors.dtos.RemoveDevices;
import co.xarx.trix.config.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * A sample supervisor which should handle exceptions and general feedback
 * for the actual {@link TaskActor}
 * <p/>
 * A router is configured at startup time, managing a pool of task actors.
 */
@Component
@Scope("prototype")
public class Supervisor extends UntypedActor {

    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), "Supervisor");

    @Autowired
    private SpringExtension springExtension;

    private Router devicesOperations;

    @Override
    public void preStart() throws Exception {

        logger.info("Starting up");

		initSupervideActors();

//        List<Routee> routees = new ArrayList<Routee>();

//        router = new Router(new SmallestMailboxRoutingLogic(), routees);
        super.preStart();
    }

	private void initSupervideActors() {
		List<Routee> routees = new ArrayList<Routee>();
		ActorRef doa = getContext().actorOf(springExtension.props("devicesOperationsActor"), "devicesOperationsActor");
		getContext().watch(doa);
		routees.add(new ActorRefRoutee(doa));

//        for (int i = 0; i < 100; i++) {
//            ActorRef actor = getContext().actorOf(springExtension.props("taskActor"));
//            getContext().watch(actor);
//            routees.add(new ActorRefRoutee(actor));
//        }

		devicesOperations = new Router(new SmallestMailboxRoutingLogic(), routees);
	}

	@Override
    public void onReceive(Object message) throws Exception {
		logger.debug("supervisor");

		routeMessage(message);
        if (message instanceof Terminated) {
            // Readd task actors if one failed
        } else {
            logger.error("Unable to handle message {}", message);
        }
    }

    public void routeMessage(Object message){
		if (message instanceof RemoveDevices)
			devicesOperations.route(message, getSender());
	}

    public void restartActor(Terminated message){
//		message.actor().
//		if(devicesOperations.routees().toList().last(). message.actor())
//		devicesOperations = devicesOperations.removeRoutee(((Terminated) message).actor());
//		ActorRef actor = getContext().actorOf(springExtension.props
//				("taskActor"));
//		getContext().watch(actor);
//		devicesOperations = devicesOperations.addRoutee(new ActorRefRoutee(actor));
	}

    @Override
    public void postStop() throws Exception {
        logger.info("Shutting down");
        super.postStop();
    }
}