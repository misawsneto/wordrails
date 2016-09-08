package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Notification;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

@Component
@RepositoryEventHandler(Notification.class)
public class NotificationEventHandler {



    @HandleAfterSave
    public void afterSave(Notification notification){

    }
}
