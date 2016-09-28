package co.xarx.trix.config;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Component
public class ApplicationLoaded implements ApplicationListener<ContextRefreshedEvent> {

	@Autowired
	private ApplicationContext applicationContext;
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent ) {
		if(contextRefreshedEvent.getSource().toString().contains("Dispatcher")) {
			ActorSystem system = applicationContext.getBean(ActorSystem.class);
			SpringExtension ext = applicationContext.getBean(SpringExtension.class);
			// Use the Spring Extension to create props for a named actor bean
			ActorRef supervisor = system.actorOf(ext.props("supervisor"), "supervisor");
		}
    }
}