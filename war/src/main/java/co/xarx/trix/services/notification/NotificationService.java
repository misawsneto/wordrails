package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import co.xarx.trix.util.ListUtil;
import org.eclipse.persistence.jpa.jpql.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
public class NotificationService {

	private AndroidNotificationSender androidNotificationSender;
	private AppleNotificationSender appleNotificationSender;

	@Autowired
	public NotificationService(AndroidNotificationSender androidNotificationSender, AppleNotificationSender appleNotificationSender) {
		this.androidNotificationSender = androidNotificationSender;
		this.appleNotificationSender = appleNotificationSender;
	}

	public List<Notification> sendAndroidNotifications(NotificationView notification, Post post, Set<String> devices) {
		Assert.isNotNull(devices, "Devices must not be null");
		Assert.isNotNull(notification, "Notification must not be null");

		Map<String, NotificationResult> results;
		List<Notification> notifications = new ArrayList<>();

		List<List<String>> androidParts = ListUtil.partition(new ArrayList<>(devices), androidNotificationSender.getBatchSize());
		for (List<String> part : androidParts) {
			try {
				results = androidNotificationSender.sendMessageToDevices(notification, part);

				notifications.addAll(this.getNotifications(results, notification, post, Notification.DeviceType.ANDROID));
			} catch (IOException e) {
				notifications.addAll(this.getNotifications(new HashSet<>(part), notification, post,
						"Failed to send to GCM", Notification.Status.SERVER_ERROR, Notification.DeviceType.ANDROID));
			}

			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
			}
		}

		return notifications;
	}

	public List<Notification> sendAppleNotifications(NotificationView notification, Post post, Set<String> devices) {
		Assert.isNotNull(devices, "Devices must not be null");
		Assert.isNotNull(notification, "Notification must not be null");

		Map<String, NotificationResult> results;
		List<Notification> notifications = new ArrayList<>();

		List<List<String>> appleParts = ListUtil.partition(new ArrayList<>(devices), appleNotificationSender.getBatchSize());
		for (List<String> part : appleParts) {
			try {
				results = appleNotificationSender.sendMessageToDevices(notification, part);

				notifications.addAll(this.getNotifications(results, notification, post, Notification.DeviceType.APPLE));
			} catch (IOException e) {
				notifications.addAll(this.getNotifications(new HashSet<>(part), notification, post,
						"Failed to send to Apple", Notification.Status.SERVER_ERROR, Notification.DeviceType.APPLE));
			}

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
		}

		return notifications;
	}

	public List<Notification> getNotifications(Set<String> devices,
												NotificationView notification,
												Post post, String errorMessage, Notification.Status status,
												Notification.DeviceType deviceType) {
		Assert.isNotNull(devices, "Null devices");
		Assert.isNotNull(notification, "Null notification");

		List<Notification> notis = new ArrayList<>();

		for (String device : devices) {
			Notification noti = new Notification(device, notification.hash, status, notification.message, notification.type);
			noti.post = post;
			noti.errorCodeName = errorMessage;
			noti.setDeviceType(deviceType);
			notis.add(noti);
		}

		return notis;
	}


	private List<Notification> getNotifications(Map<String, NotificationResult> results,
												NotificationView notification, Post post,
												Notification.DeviceType deviceType) {
		Assert.isNotNull(results, "Null results");
		Assert.isNotNull(notification, "Null notification");

		List<Notification> notis = new ArrayList<>();

		for (String device : results.keySet()) {
			NotificationResult r = results.get(device);
			Notification noti = new Notification(device, notification.hash, r.status, notification.message, notification.type);
			noti.post = post;
			noti.errorCodeName = r.errorMessage;
			noti.setDeviceType(deviceType);
			notis.add(noti);
		}

		return notis;
	}
}
