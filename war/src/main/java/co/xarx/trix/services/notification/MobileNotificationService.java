package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.services.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class MobileNotificationService {

	@Value("${spring.profiles.active:'dev'}")
	private String profile;

	private NotificationBatchPublisher notificationBatchPublisher;
	private AsyncService asyncService;
	private MobileNotificationSender appleNS;
	private MobileNotificationSender androidNS;

	@Autowired
	public MobileNotificationService(NotificationBatchPublisher notificationBatchPublisher,
									 AsyncService asyncService,
									 MobileNotificationSender appleNS, MobileNotificationSender androidNS) {
		this.notificationBatchPublisher = notificationBatchPublisher;
		this.asyncService = asyncService;
		this.appleNS = appleNS;
		this.androidNS = androidNS;
	}

//	@AccessGroup(tenants = {"demo"}, profiles = {"prod"}, inclusion = true)
	public List<MobileNotification> sendNotifications(NotificationView notification,
													  Collection<String> androidDevices, Collection<String> appleDevices) throws NotificationException {
		Future<List<MobileNotification>> futureAndroidNotifications = null;
		Future<List<MobileNotification>> futureAppleNotifications = null;
		try {
			futureAndroidNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
					() -> notificationBatchPublisher.sendNotifications(androidNS, notification, androidDevices, MobileNotification.DeviceType.ANDROID));
			futureAppleNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
					() -> notificationBatchPublisher.sendNotifications(appleNS, notification, appleDevices, MobileNotification.DeviceType.APPLE));
		} catch (Exception e) {
			if (futureAndroidNotifications != null)
				futureAndroidNotifications.cancel(true);
			throw new NotificationException(e);
		}

		List<MobileNotification> mobileNotifications;

		try {
			mobileNotifications = futureAndroidNotifications.get();
		} catch (InterruptedException | ExecutionException e) {
			mobileNotifications = notificationBatchPublisher.getErrorNotifications(androidDevices,
					notification, e, MobileNotification.DeviceType.ANDROID);
		}

		try {
			mobileNotifications.addAll(futureAppleNotifications.get());
		} catch (InterruptedException | ExecutionException e) {
			mobileNotifications.addAll(notificationBatchPublisher.getErrorNotifications(appleDevices,
					notification, e, MobileNotification.DeviceType.APPLE));
		}

		return mobileNotifications;
	}
}
