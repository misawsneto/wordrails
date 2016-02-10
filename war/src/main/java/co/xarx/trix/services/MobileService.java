package co.xarx.trix.services;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.exception.OperationNotSupportedException;
import co.xarx.trix.persistence.MobileDeviceRepository;
import co.xarx.trix.persistence.NetworkRepository;
import co.xarx.trix.persistence.NotificationRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import co.xarx.trix.services.notification.NotificationService;
import co.xarx.trix.util.StringUtil;
import com.mysema.commons.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class MobileService {

	@Value("${spring.profiles.active:'dev'}")
	private String profile;

	@Autowired
	public NotificationService notificationService;
	@Autowired
	private AsyncService asyncService;
	@Autowired
	private PostConverter postConverter;
	@Autowired
	private TrixAuthenticationProvider authProvider;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private MobileDeviceRepository mobileDeviceRepository;
	@Autowired
	private NotificationRepository notificationRepository;

	public void buildNewPostNotification(Post post, List<MobileDevice> mobileDevices) throws NotificationException {
		Assert.notNull(post, "Post should not be null");

		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId); //this should be removed in next android releases

		NotificationView notification = getCreatePostNotification(post, network.id);

		if(notification.test && !tenantId.equals("demo")) {
			throw new OperationNotSupportedException("Notifications can be sent from dev profile only in DEMO");
		}

		Set<String> androidDevices = new HashSet<>();
		Set<String> appleDevices = new HashSet<>();
		mobileDevices.stream().filter(device -> !post.author.equals(device.person)).forEach(device -> {
			if (device.type.equals(MobileDevice.Type.ANDROID)) {
				androidDevices.add(device.deviceCode);
			} else if (device.type.equals(MobileDevice.Type.APPLE)) {
				appleDevices.add(device.deviceCode);
			}
		});

		Future<List<Notification>> futureAndroidNotifications = null;
		Future<List<Notification>> futureAppleNotifications = null;
		try {
			futureAndroidNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
					() -> notificationService.sendAndroidNotifications(notification, post, androidDevices));
			futureAppleNotifications = asyncService.run(TenantContextHolder.getCurrentTenantId(),
					() -> notificationService.sendAppleNotifications(notification, post, appleDevices));
		} catch (Exception e) {
			if (futureAndroidNotifications != null) futureAndroidNotifications.cancel(true);
			throw new NotificationException(e);
		}


		List<Notification> androidNotifications;
		try {
			androidNotifications = futureAndroidNotifications.get();
		} catch (InterruptedException | ExecutionException e) {
			androidNotifications = notificationService.getNotifications(androidDevices,
					notification, post, "Execution error",
					Notification.Status.SEND_ERROR, Notification.DeviceType.ANDROID);
		}

		for (Notification androidNotification : androidNotifications) {
			notificationRepository.save(androidNotification);
		}


		List<Notification> appleNotifications;
		try {
			appleNotifications = futureAppleNotifications.get();
		} catch (InterruptedException | ExecutionException e) {
			appleNotifications = notificationService.getNotifications(appleDevices,
					notification, post, "Execution error",
					Notification.Status.SEND_ERROR, Notification.DeviceType.APPLE);
		}

		for (Notification appleNotification : appleNotifications) {
			notificationRepository.save(appleNotification);
		}
	}

	public NotificationView getCreatePostNotification(Post post, Integer networkId) {
		NotificationView notification = new NotificationView();
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.message = post.title;
		notification.networkId = networkId;
		notification.post = postConverter.convertTo(post);
		notification.postId = post.id;
		notification.postTitle = post.title;
		notification.postSnippet = StringUtil.simpleSnippet(post.body);
		notification.test = !profile.equals("prod");
		return notification;
	}

	public void updateDevice(String deviceCode, Double lat, Double lng) {
		Assert.hasText(deviceCode, "Device code must not be empty");

		MobileDevice device = mobileDeviceRepository.findOne(QMobileDevice.mobileDevice.deviceCode.eq(deviceCode));
		if (device == null || device.deviceCode == null) {
			Person person = authProvider.getLoggedPerson();

			if (person.id == 0)
				person = null;

			device.person = person;

			if (lat != null) {
				device.lat = lat;
			}
			if (lng != null) {
				device.lng = lng;
			}
			mobileDeviceRepository.save(device);
		}
	}
}
