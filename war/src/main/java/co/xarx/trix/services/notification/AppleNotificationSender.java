package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AppleCertificate;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.persistence.AppleCertificateRepository;
import com.notnoop.apns.APNS;
import com.notnoop.apns.ApnsNotification;
import com.notnoop.apns.ApnsService;
import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ClientNotConnectedException;
import com.relayrides.pushy.apns.PushNotificationResponse;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Component
public class AppleNotificationSender implements NotificationSender {

	private AppleCertificateRepository appleCertificateRepository;

	//needed for unit testing
	AppleNotificationSender() {
	}

	@Autowired
	public AppleNotificationSender(AppleCertificateRepository appleCertificateRepository) {
		this.appleCertificateRepository = appleCertificateRepository;
	}

	public ApnsService getAPNsClient(Boolean isTest) throws SQLException {

		String tenantId = TenantContextHolder.getCurrentTenantId();
		AppleCertificate certificate = appleCertificateRepository.findByTenantId(tenantId);

		ApnsService service = APNS
				.newService()
				.withCert(certificate.file.getBinaryStream(), certificate.password)
				.withProductionDestination()
				.build();

		return service;
	}

	@Override
	public Map<String, NotificationResult> sendMessageToDevices(NotificationView notification, Collection<String> devices) throws IOException {
//		ApnsClient<SimpleApnsPushNotification> apnsClient;

		ApnsService apnsClient = null;
		try {
			apnsClient = getAPNsClient(notification.test);
		} catch (Exception e) {
			throw new IOException("Failed to initialize Apple Notification Service");
		}

		String payload = APNS.newPayload()
				.badge(1)
				.sound("default")
				.customField("postId", notification.postId)
				.customField("title", notification.title)
				.alertBody(notification.message)
				.customField("content-available", true)
				.shrinkBody("...")
				.build();

		Collection<ApnsNotification> apnsNotifications = (Collection<ApnsNotification>) apnsClient.push(devices, payload);

		Map<String, NotificationResult> notificationResultMap = new HashMap<>();
		Map<String, Date> inactiveDevices = apnsClient.getInactiveDevices();

		for (ApnsNotification apnsNotification : apnsNotifications) {
			NotificationResult r = new NotificationResult();

			if(inactiveDevices.containsKey(apnsNotification.getDeviceToken().toString())){
				r.setStatus(Notification.Status.SEND_ERROR);
				r.setErrorMessage("Device deactivated");
				r.setDeviceDeactivated(true);
			} else {
				r.setStatus(Notification.Status.SUCCESS);
			}

			notificationResultMap.put(apnsNotification.getDeviceToken().toString(), r);
		}

		return notificationResultMap;
	}

	PushNotificationResponse get(ApnsClient<SimpleApnsPushNotification> apnsClient, SimpleApnsPushNotification apnsNotification) throws ExecutionException, InterruptedException {
		return apnsClient.sendNotification(apnsNotification).get();
	}

	private Map<String, NotificationResult> sendMessageToDevices(String payloader, List<String> devices, ApnsClient<SimpleApnsPushNotification> apnsClient, Integer index, Map<String, NotificationResult> results) throws IOException {
		int i = index;
		try {
			int notificationsCounter = 0;
			for (i = index; i < devices.size(); i++) {
				String deviceCode = devices.get(i);

				SimpleApnsPushNotification apnsNotification = new SimpleApnsPushNotification(TokenUtil.sanitizeTokenString(deviceCode), "topic", payloader);

				PushNotificationResponse response = get(apnsClient, apnsNotification);

				NotificationResult notificationResult = new NotificationResult();
				if (response.isAccepted()) {
					notificationResult.setStatus(Notification.Status.SUCCESS);
				} else {
					notificationResult.setStatus(Notification.Status.SERVER_ERROR);
					notificationResult.setErrorMessage(response.getRejectionReason());

					if (response.getTokenInvalidationTimestamp() != null) {
						notificationResult.setDeviceDeactivated(true);
					}
				}

				results.put(deviceCode, notificationResult);

				if (notificationsCounter >= getBatchSize()) {
					Thread.sleep(1000);
					notificationsCounter = 0;
				}
				notificationsCounter++;
			}
		} catch (InterruptedException e) {
			throw new IOException("Connection was interrupted", e);
		} catch (ExecutionException e) {
			if (e.getCause() instanceof ClientNotConnectedException) {
				try {
					apnsClient.getReconnectionFuture().await();
				} catch (InterruptedException e1) {
					throw new IOException("Failed to reconnect to Apple", e);
				}
				return sendMessageToDevices(payloader, devices, apnsClient, i, results);
			} else {
				throw new IOException("Unexpected error", e);
			}
		}

		return results;
	}

	@Override
	public Integer getBatchSize() {
		return 8999;
	}
}
