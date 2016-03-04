package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Notification;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MobileNotificationSender implements NotificationSender {

	private NotificationServerClient client;
	private Integer batchSize;

	public MobileNotificationSender(NotificationServerClient client, Integer batchSize) {
		this.client = client;
		this.batchSize = batchSize;
	}

	@Override
	public Map<String, NotificationResult> sendMessageToDevices(NotificationView notification, Collection<String> devices) throws IOException {

		client.send(notification, devices);

		Map<String, NotificationResult> notificationResultMap = new HashMap<>();
		Map<String, NotificationResult> errorDevices = client.getErrorDevices();

		for (String device : devices) {
			NotificationResult r = errorDevices.get(device);

			if (r == null) {
				r = new NotificationResult();
				r.setStatus(Notification.Status.SUCCESS);
			}

			notificationResultMap.put(device, r);
		}

		return notificationResultMap;
	}

	@Override
	public Integer getBatchSize() {
		return batchSize;
	}
}
