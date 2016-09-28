package co.xarx.trix.services.notification.stubs;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.services.notification.NotificationResult;
import co.xarx.trix.services.notification.NotificationServerClient;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NotificationServerClientError implements NotificationServerClient {

	@Override
	public Map<String, NotificationResult> getErrorDevices() {
		return null; //must not enter here
	}

	@Override
	public void send(NotificationView notification, Collection<String> devices) throws IOException {
		throw new IOException();
	}

	@Override
	public Map<String, NotificationResult> getSuccessDevices() {
		return new HashMap<>();
	}
}
