package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.MobileNotification;
import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.services.AsyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
	private MobileNotificationSender androidFcmNS;
	private MobileNotificationSender appleFcmNS;

	private MobileNotificationSender android2FcmNS;
	private MobileNotificationSender apple2FcmNS;

	@Autowired
	public MobileNotificationService(NotificationBatchPublisher notificationBatchPublisher,
									 AsyncService asyncService,
									 MobileNotificationSender appleNS, MobileNotificationSender androidNS,
									 MobileNotificationSender androidFcmNS, MobileNotificationSender appleFcmNS,

									 MobileNotificationSender android2FcmNS, MobileNotificationSender apple2FcmNS) {

		this.notificationBatchPublisher = notificationBatchPublisher;
		this.asyncService = asyncService;
		this.appleNS = appleNS;
		this.androidNS = androidNS;
		this.androidFcmNS = androidFcmNS;
		this.appleFcmNS = appleFcmNS;

		this.android2FcmNS = android2FcmNS;
		this.apple2FcmNS = apple2FcmNS;
	}

//	@AccessGroup(tenants = {"demo"}, profiles = {"prod"}, inclusion = true)
	public List<MobileNotification> sendNotifications(NotificationView notification,
													  Collection<String> androidDevices, Collection<String> appleDevices, Collection<String> fcmAndroidDevices, Collection<String> fcmAppleDevices) throws NotificationException {
		Future<List<MobileNotification>> futureAndroidNotifications = null;
		Future<List<MobileNotification>> futureAppleNotifications = null;
		Future<List<MobileNotification>> futureFcmAndroidNotifications = null;
		Future<List<MobileNotification>> futureFcmAppleNotifications = null;
		try {
			if(androidDevices != null && !androidDevices.isEmpty())
				futureAndroidNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
						() -> notificationBatchPublisher.sendNotifications(androidNS, notification, androidDevices, MobileNotification.DeviceType.ANDROID));

			if(appleDevices != null && !appleDevices.isEmpty())
				futureAppleNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
						() -> notificationBatchPublisher.sendNotifications(appleNS, notification, appleDevices, MobileNotification.DeviceType.APPLE));

			if(fcmAndroidDevices != null && !fcmAndroidDevices.isEmpty())
				futureFcmAndroidNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
						() -> notificationBatchPublisher.sendNotifications(androidFcmNS, notification, fcmAndroidDevices, MobileNotification.DeviceType.FCM_ANDROID));

			if(fcmAppleDevices != null && !fcmAppleDevices.isEmpty())
				futureFcmAppleNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
						() -> notificationBatchPublisher.sendNotifications(appleFcmNS, notification, fcmAppleDevices,
								MobileNotification.DeviceType.FCM_APPLE));
		} catch (Exception e) {
			if (futureAndroidNotifications != null)
				futureAndroidNotifications.cancel(true);
			throw new NotificationException(e);
		}

		List<MobileNotification> mobileNotifications = new ArrayList<>();

		try {
			if(futureAndroidNotifications != null)
				mobileNotifications.addAll(futureAndroidNotifications.get());
		} catch (InterruptedException | ExecutionException e) {
			mobileNotifications = notificationBatchPublisher.getErrorNotifications(androidDevices,
					notification, e, MobileNotification.DeviceType.ANDROID);
		}

		try {
			if(futureAppleNotifications != null)
				mobileNotifications.addAll(futureAppleNotifications.get());
		} catch (InterruptedException | ExecutionException e) {
			mobileNotifications.addAll(notificationBatchPublisher.getErrorNotifications(appleDevices,
					notification, e, MobileNotification.DeviceType.APPLE));
		}

		try {
			if(futureFcmAndroidNotifications != null)
				mobileNotifications.addAll(futureFcmAndroidNotifications.get());
		} catch (InterruptedException | ExecutionException e) {
			mobileNotifications = notificationBatchPublisher.getErrorNotifications(fcmAndroidDevices,
					notification, e, MobileNotification.DeviceType.FCM_ANDROID);
		}

		try {
			if(futureFcmAppleNotifications != null)
				mobileNotifications.addAll(futureFcmAppleNotifications.get());
		} catch (InterruptedException | ExecutionException e) {
			mobileNotifications = notificationBatchPublisher.getErrorNotifications(fcmAppleDevices,
					notification, e, MobileNotification.DeviceType.FCM_APPLE);
		}

		return mobileNotifications;
	}

	public List<MobileNotification> sendNotifications2(NotificationView notification, Collection fcm2AndroidDevices, Collection fcm2AppleDevices) {
		Future<List<MobileNotification>> futureFcm2AndroidNotifications = null;
		Future<List<MobileNotification>> futureFcm2AppleNotifications = null;
		try {
			if(fcm2AndroidDevices != null && !fcm2AndroidDevices.isEmpty())
				futureFcm2AndroidNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
						() -> notificationBatchPublisher.sendNotifications(android2FcmNS, notification, fcm2AndroidDevices,
								MobileNotification.DeviceType.FCM_ANDROID2));

			if(fcm2AppleDevices != null && !fcm2AppleDevices.isEmpty())
				futureFcm2AppleNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
						() -> notificationBatchPublisher.sendNotifications(apple2FcmNS, notification, fcm2AppleDevices,
								MobileNotification.DeviceType.FCM_APPLE2));
		} catch (Exception e) {
			throw new NotificationException(e);
		}

		List<MobileNotification> mobileNotifications = new ArrayList<>();

		try {
			if(futureFcm2AndroidNotifications != null)
				mobileNotifications.addAll(futureFcm2AndroidNotifications.get());
		} catch (InterruptedException | ExecutionException e) {
			mobileNotifications = notificationBatchPublisher.getErrorNotifications(fcm2AndroidDevices,
					notification, e, MobileNotification.DeviceType.FCM_ANDROID2);
		}

		try {
			if(futureFcm2AppleNotifications != null)
				mobileNotifications.addAll(futureFcm2AppleNotifications.get());
		} catch (InterruptedException | ExecutionException e) {
			mobileNotifications = notificationBatchPublisher.getErrorNotifications(fcm2AppleDevices,
					notification, e, MobileNotification.DeviceType.FCM_APPLE2);
		}

		return mobileNotifications;
	}
}
