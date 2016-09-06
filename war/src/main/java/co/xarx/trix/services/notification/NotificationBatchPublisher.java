package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.services.AccessService;
import co.xarx.trix.util.ListUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.jcodec.common.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class NotificationBatchPublisher {

	@Autowired
	private AccessService accessService;

	public List<MobileNotification> sendNotifications(NotificationSender sender, NotificationView notification,
													  Collection<String> devices, MobileNotification.DeviceType deviceType) {

		Logger.info("Sending notification. " + devices.size() + " devices. Type: " + deviceType.name());

		if(!(accessService.hasPermissionOnTenant(true, "demo") || accessService.hasPermissionOnProfile(true, "prod"))) {
			return new ArrayList<MobileNotification>();
		}

		Assert.notEmpty(devices, "Devices must not be null and not empty");
		Assert.notNull(notification, "Notification must not be null");

		Map<String, NotificationResult> results;
		List<MobileNotification> mobileNotifications = new ArrayList<>();

		List<List<String>> parts = ListUtil.partition(new ArrayList<>(devices), sender.getBatchSize());
		sender.start();
		for (int i = 0; i < parts.size(); i++) {
			List<String> part = parts.get(i);
			try {
				Logger.info(deviceType.toString() + ": Sending notification to " +
						devices.size() + " devices. Notification hash: " + notification.hash);
				results = sender.sendMessageToDevices(notification, part);

				mobileNotifications.addAll(this.getSuccessNotifications(results, notification, deviceType));
			} catch (IOException e) {
				log.error("Error sending notification to devices", e);
				mobileNotifications.addAll(this.getErrorNotifications(new HashSet<>(part), notification, e, deviceType));
			}

			boolean isNotLast = i + 1 < parts.size();
			if (isNotLast) {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
				}
			}
		}
		sender.stop();
		Logger.info("Finish sending notification");

		return mobileNotifications;
	}

	public List<MobileNotification> getErrorNotifications(Collection<String> devices,
														  NotificationView notification, Exception e,
														  MobileNotification.DeviceType deviceType) {
		List<MobileNotification> notis = new ArrayList<>();
		if(devices == null || devices.isEmpty())
			return notis;

		Assert.notNull(notification, "Notification must not be null");

		for (String device : devices) {
			MobileNotification noti = new MobileNotification(device, notification.hash, MobileNotification.Status.SEND_ERROR, notification.message, notification.type);
			noti.errorCodeName = "Failed to send notification to server";
			noti.setDeviceType(deviceType);
			noti.deviceDeactivated = false;
			noti.setStackTrace(ExceptionUtils.getStackTrace(e));
			noti.test = notification.test;
			notis.add(noti);
		}

		return notis;
	}


	private List<MobileNotification> getSuccessNotifications(Map<String, NotificationResult> results,
															 NotificationView notification,
															 MobileNotification.DeviceType deviceType) {
		List<MobileNotification> notis = new ArrayList<>();
		if (results == null || results.isEmpty())
			return notis;

		Assert.notNull(notification, "Notification must not be null");

		for (String device : results.keySet()) {
			NotificationResult r = results.get(device);
			MobileNotification noti = new MobileNotification(device, notification.hash, r.getStatus(), notification.message, notification.type);
			noti.errorCodeName = r.getErrorMessage();
			noti.setDeviceType(deviceType);
			noti.deviceDeactivated = r.isDeviceDeactivated();
			noti.test = notification.test;
			notis.add(noti);
		}

		return notis;
	}
}
