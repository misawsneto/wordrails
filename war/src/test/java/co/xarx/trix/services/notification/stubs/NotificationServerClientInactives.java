package co.xarx.trix.services.notification.stubs;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.services.notification.NotificationResult;
import co.xarx.trix.services.notification.NotificationServerClient;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class NotificationServerClientInactives implements NotificationServerClient {

	@Override
	public Map<String, NotificationResult> getErrorDevices() {
		Map<String, NotificationResult> deactivated = new HashMap<>();
		NotificationResult value = new NotificationResult();
		NotificationResult value2 = new NotificationResult();
		value.setStatus(MobileNotification.Status.SERVER_ERROR);
		value2.setStatus(MobileNotification.Status.SERVER_ERROR);
		value2.setDeviceDeactivated(true);
		deactivated.put("device1", value);
		deactivated.put("device2", value2);
		return deactivated;
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
