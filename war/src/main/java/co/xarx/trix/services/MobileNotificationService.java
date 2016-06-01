package co.xarx.trix.services;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.aspect.annotations.AccessGroup;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Post;
import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.services.notification.MobileNotificationSender;
import co.xarx.trix.services.notification.NotificationService;
import com.mysema.commons.lang.Assert;
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

	private NotificationService notificationService;
	private AsyncService asyncService;
	private MobileNotificationSender appleNS;
	private MobileNotificationSender androidNS;

	@Autowired
	public MobileNotificationService(NotificationService notificationService,
									 AsyncService asyncService,
									 MobileNotificationSender appleNS, MobileNotificationSender androidNS) {
		this.notificationService = notificationService;
		this.asyncService = asyncService;
		this.appleNS = appleNS;
		this.androidNS = androidNS;
	}

	@AccessGroup(tenants = {"demo"}, profiles = {"prod"}, inclusion = true)
	public List<Notification> sendNotifications(Post post, NotificationView notification,
												Collection<String> androidDevices, Collection<String> appleDevices) throws NotificationException {
		Assert.notNull(post, "Post should not be null");

		Future<List<Notification>> futureAndroidNotifications = null;
		Future<List<Notification>> futureAppleNotifications;
		try {
			futureAndroidNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
					() -> notificationService.sendNotifications(androidNS, notification, post, androidDevices, Notification.DeviceType.ANDROID));
			futureAppleNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
					() -> notificationService.sendNotifications(appleNS, notification, post, appleDevices, Notification.DeviceType.APPLE));
		} catch (Exception e) {
			if (futureAndroidNotifications != null)
				futureAndroidNotifications.cancel(true);
			throw new NotificationException(e);
		}

		List<Notification> notifications;

		try {
			notifications = futureAndroidNotifications.get();
		} catch (InterruptedException | ExecutionException e) {
			notifications = notificationService.getErrorNotifications(androidDevices,
					notification, post, e, Notification.DeviceType.ANDROID);
		}

		try {
			notifications.addAll(futureAppleNotifications.get());
		} catch (InterruptedException | ExecutionException e) {
			notifications.addAll(notificationService.getErrorNotifications(appleDevices,
					notification, post, e, Notification.DeviceType.APPLE));
		}

		return notifications;
	}
}
