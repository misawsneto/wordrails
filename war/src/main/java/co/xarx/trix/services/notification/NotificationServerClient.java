package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public interface NotificationServerClient {

	Map<String, NotificationResult> getErrorDevices();

	void send(NotificationView notification, Collection<String> devices) throws IOException;

	Map<String,NotificationResult> getSuccessDevices();
}
