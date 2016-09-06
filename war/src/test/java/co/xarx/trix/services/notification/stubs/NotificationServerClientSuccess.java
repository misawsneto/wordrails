package co.xarx.trix.services.notification.stubs;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.services.notification.NotificationResult;
import co.xarx.trix.services.notification.NotificationServerClient;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NotificationServerClientSuccess implements NotificationServerClient {

	@Override
	public Map<String, NotificationResult> getErrorDevices() {
		return new HashMap<>();
	}

	@Override
	public void send(NotificationView notification, Collection<String> devices) throws IOException {
	}

	@Override
	public void start() {

	}

	@Override
	public void stop() {

	}
}
