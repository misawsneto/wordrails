package co.xarx.trix.services;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.aspect.annotations.TenantAuthorize;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.persistence.MobileDeviceRepository;
import co.xarx.trix.services.notification.AndroidNotificationSender;
import co.xarx.trix.services.notification.AppleNotificationSender;
import co.xarx.trix.services.notification.NotificationService;
import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class MobileService {

	@Value("${spring.profiles.active:'dev'}")
	private String profile;

	private NotificationService notificationService;
	private AsyncService asyncService;
	private MobileDeviceRepository mobileDeviceRepository;
	private AppleNotificationSender appleNS;
	private AndroidNotificationSender androidNS;

	@Autowired
	public MobileService(NotificationService notificationService,
						 AsyncService asyncService, MobileDeviceRepository mobileDeviceRepository,
						 AppleNotificationSender appleNS, AndroidNotificationSender androidNS) {
		this.notificationService = notificationService;
		this.asyncService = asyncService;
		this.mobileDeviceRepository = mobileDeviceRepository;
		this.appleNS = appleNS;
		this.androidNS = androidNS;
	}

	public Collection<String> getDeviceCodes(List<MobileDevice> mobileDevices, MobileDevice.Type type) {
		Set<String> deviceCodes = new HashSet<>();
		mobileDevices.stream().forEach(device -> {
			if (type.equals(device.type)) {
				deviceCodes.add(device.deviceCode);
			}
		});

		return deviceCodes;
	}

	@TenantAuthorize(tenants = {"demo"}, profiles = {"dev"})
	public List<Notification> sendNotifications(Post post, NotificationView notification,
												Collection<String> androidDevices, Collection<String> appleDevices) throws NotificationException {
		Assert.notNull(post, "Post should not be null");

		Future<List<Notification>> futureAndroidNotifications = null;
		Future<List<Notification>> futureAppleNotifications = null;
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
					notification, post, Notification.DeviceType.ANDROID);
		}

		try {
			notifications.addAll(futureAppleNotifications.get());
		} catch (InterruptedException | ExecutionException e) {
			notifications.addAll(notificationService.getErrorNotifications(appleDevices,
					notification, post, Notification.DeviceType.APPLE));
		}

		return notifications;
	}

	public void updateDevice(Person person, String deviceCode, Double lat, Double lng, MobileDevice.Type type) {
		Assert.hasText(deviceCode, "Device code must not be empty");

		MobileDevice device = mobileDeviceRepository.findOne(QMobileDevice.mobileDevice.deviceCode.eq(deviceCode));

		device = getMobileDevice(person, deviceCode, lat, lng, type, device);

		mobileDeviceRepository.save(device);
	}

	public MobileDevice getMobileDevice(Person person, String deviceCode,
										Double lat, Double lng, MobileDevice.Type type, MobileDevice device) {
		if (person != null && person.id == 0) {
			person = null;
		}

		if (device == null) {
			device = new MobileDevice();

			device.deviceCode = deviceCode;
			device.person = person;
			device.active = true;
			device.type = type;
		}

		device.person = person;
		if (lat != null) device.lat = lat;
		if (lng != null) device.lng = lng;

		return device;
	}
}
