package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface NotificationSender {

	Map<String, NotificationResult> sendMessageToDevices(NotificationView notification, Collection<String> devices) throws IOException;

	Integer getBatchSize();
}
