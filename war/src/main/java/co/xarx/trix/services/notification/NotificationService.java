package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import co.xarx.trix.util.ListUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

@Service
public class NotificationService {

	public List<Notification> sendNotifications(NotificationSender sender, NotificationView notification,
												Post post, Collection<String> devices, Notification.DeviceType deviceType) {
		Assert.notEmpty(devices, "Devices must not be null and not empty");
		Assert.notNull(notification, "Notification must not be null");

		Map<String, NotificationResult> results;
		List<Notification> notifications = new ArrayList<>();

		List<List<String>> parts = ListUtil.partition(new ArrayList<>(devices), sender.getBatchSize());
		for (List<String> part : parts) {
			try {
				results = sender.sendMessageToDevices(notification, part);

				notifications.addAll(this.getSuccessNotifications(results, notification, post, deviceType));
			} catch (IOException e) {
				notifications.addAll(this.getErrorNotifications(new HashSet<>(part), notification, post, deviceType));
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}

		return notifications;
	}

	public List<Notification> getErrorNotifications(Collection<String> devices,
													NotificationView notification,
													Post post,
													Notification.DeviceType deviceType) {
		List<Notification> notis = new ArrayList<>();
		if(devices == null || devices.isEmpty())
			return notis;

		Assert.notNull(notification, "Notification must not be null");


		for (String device : devices) {
			Notification noti = new Notification(device, notification.hash, Notification.Status.SEND_ERROR, notification.message, notification.type);
			noti.post = post;
			noti.errorCodeName = "Failed to send notification to server";
			noti.setDeviceType(deviceType);
			noti.deviceDeactivated = false;
			notis.add(noti);
		}

		return notis;
	}


	private List<Notification> getSuccessNotifications(Map<String, NotificationResult> results,
													   NotificationView notification, Post post,
													   Notification.DeviceType deviceType) {
		List<Notification> notis = new ArrayList<>();
		if (results == null || results.isEmpty())
			return notis;

		Assert.notNull(notification, "Notification must not be null");

		for (String device : results.keySet()) {
			NotificationResult r = results.get(device);
			Notification noti = new Notification(device, notification.hash, r.getStatus(), notification.message, notification.type);
			noti.post = post;
			noti.errorCodeName = r.getErrorMessage();
			noti.setDeviceType(deviceType);
			noti.deviceDeactivated = r.isDeviceDeactivated();
			notis.add(noti);
		}

		return notis;
	}
}
