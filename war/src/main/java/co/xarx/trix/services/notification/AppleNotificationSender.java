package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.AppleCertificate;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.persistence.AppleCertificateRepository;
import co.xarx.trix.util.FileUtil;
import com.relayrides.pushy.apns.ApnsClient;
import com.relayrides.pushy.apns.ClientNotConnectedException;
import com.relayrides.pushy.apns.PushNotificationResponse;
import com.relayrides.pushy.apns.util.ApnsPayloadBuilder;
import com.relayrides.pushy.apns.util.SimpleApnsPushNotification;
import com.relayrides.pushy.apns.util.TokenUtil;
import io.netty.util.concurrent.Future;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Component
public class AppleNotificationSender implements NotificationSender {

	private AppleCertificateRepository appleCertificateRepository;

	@Autowired
	public AppleNotificationSender(AppleCertificateRepository appleCertificateRepository) {
		this.appleCertificateRepository = appleCertificateRepository;
	}

	private ApnsClient<SimpleApnsPushNotification> getAPNsCliens(Boolean isTest) throws SQLException, CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException, InterruptedException {
		String tenantId = TenantContextHolder.getCurrentTenantId();
		AppleCertificate certificate = appleCertificateRepository.findByTenantId(tenantId);

		File file = FileUtil.createNewTempFile(certificate.file.getBinaryStream());

		ApnsClient<SimpleApnsPushNotification> apnsClient = new ApnsClient<>(file, certificate.password);

		Future<Void> connectFuture;
		if (isTest) {
			connectFuture = apnsClient.connect(ApnsClient.DEVELOPMENT_APNS_HOST);
		} else {
			connectFuture = apnsClient.connect(ApnsClient.PRODUCTION_APNS_HOST);
		}
		connectFuture.await();

		return apnsClient;
	}

	@Override
	public Map<String, NotificationResult> sendMessageToDevices(NotificationView notification, List<String> devices) throws IOException {
		ApnsClient<SimpleApnsPushNotification> apnsClient;
		try {
			apnsClient = getAPNsCliens(notification.test);
		} catch (SQLException | CertificateException | UnrecoverableKeyException |
				NoSuchAlgorithmException | KeyStoreException | KeyManagementException |InterruptedException e) {
			throw new IOException("Failed to initialize Apple PushManager", e);
		}

		ApnsPayloadBuilder payloadBuilder = new ApnsPayloadBuilder();
		payloadBuilder.setBadgeNumber(1);
		payloadBuilder.setSoundFileName("default");
		payloadBuilder.addCustomProperty("postId", notification.postId);
		payloadBuilder.setAlertTitle(notification.title);
		payloadBuilder.setAlertBody(notification.message);
		payloadBuilder.setContentAvailable(true);

		return sendMessageToDevices(payloadBuilder.buildWithDefaultMaximumLength(), devices, apnsClient, 0, new HashMap<>());
	}

	public Map<String, NotificationResult> sendMessageToDevices(String payloader, List<String> devices,
																ApnsClient<SimpleApnsPushNotification> apnsClient,
																Integer index, Map<String, NotificationResult> results) throws IOException {
		int i = index;
		try {
			int notificationsCounter = 0;
			for (;i < devices.size(); i++) {
				String deviceCode =  devices.get(i);

				SimpleApnsPushNotification apnsNotification =
						new SimpleApnsPushNotification(TokenUtil.sanitizeTokenString(deviceCode), "topic", payloader);

				Future<PushNotificationResponse<SimpleApnsPushNotification>> responseFuture = apnsClient.sendNotification(apnsNotification);
				PushNotificationResponse<SimpleApnsPushNotification> response = responseFuture.get();

				NotificationResult notificationResult = new NotificationResult();
				if (response.isAccepted()) {
					notificationResult.status = Notification.Status.SUCCESS;
				} else {
					notificationResult.status = Notification.Status.SERVER_ERROR;
					notificationResult.errorMessage = response.getRejectionReason();

					if (response.getTokenInvalidationTimestamp() != null) {
						notificationResult.deviceDeactivated = true;
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
			}
		}

		return results;
	}

	@Override
	public Integer getBatchSize() {
		return 8999;
	}
}
