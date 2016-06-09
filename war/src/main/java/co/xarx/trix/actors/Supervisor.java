//package co.xarx.trix.actors;
//
//import akka.actor.UntypedActor;
//import akka.event.Logging;
//import akka.event.LoggingAdapter;
//import akka.routing.Router;
//import co.xarx.trix.config.SpringExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Scope;
//import org.springframework.stereotype.Component;
//
///**
// * A sample supervisor which should handle exceptions and general feedback
// * for the actual {@link TaskActor}
// * <p/>
// * A router is configured at startup time, managing a pool of task actors.
// */
//@Component
//@Scope("prototype")
//public class Supervisor extends UntypedActor {
//
//    private final LoggingAdapter logger = Logging.getLogger(getContext().system(), "Supervisor");
//
//    @Autowired
//    private SpringExtension springExtension;
//
//    private Router router;
//
//    @Override
//    public void preStart() throws Exception {
//
//        logger.info("Starting up");
////
////        List<Routee> routees = new ArrayList<Routee>();
////        for (int i = 0; i < 100; i++) {
////            ActorRef actor = getContext().actorOf(springExtension.props("taskActor"));
////            getContext().watch(actor);
////            routees.add(new ActorRefRoutee(actor));
////        }
////        router = new Router(new SmallestMailboxRoutingLogic(), routees);
//        super.preStart();
//    }
//
//    @Override
//    public void onReceive(Object message) throws Exception {
//		logger.debug("supervisor");
////        if (message instanceof Task) {
////            router.route(message, getSender());
////        } else if (message instanceof Terminated) {
////            // Readd task actors if one failed
////            router = router.removeRoutee(((Terminated) message).actor());
////            ActorRef actor = getContext().actorOf(springExtension.props
////                ("taskActor"));
////            getContext().watch(actor);
////            router = router.addRoutee(new ActorRefRoutee(actor));
////        } else {
////            log.error("Unable to handle message {}", message);
////        }
//    }
//
//    @Override
//    public void postStop() throws Exception {
//        logger.info("Shutting down");
//        super.postStop();
//    }
//}