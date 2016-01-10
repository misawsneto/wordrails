package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.PostRead;
import co.xarx.trix.services.LogService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@RepositoryEventHandler(PostRead.class)
@Component
public class PostReadEventHandler {

	Logger log = Logger.getLogger(this.getClass().getName());

	@Autowired
	private LogService logService;

	@HandleAfterCreate
	public void handleAfterCreate(PostRead read) {
		logService.postRead(read);
		log.info("Creating PostRead");
	}

	@HandleAfterSave
	public void handleAfterSave(PostRead read) {
		logService.postRead(read);
		log.info("Updating PostRead");
	}
}
