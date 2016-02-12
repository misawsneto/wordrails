package co.xarx.trix.services;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.exception.OperationNotSupportedException;
import co.xarx.trix.persistence.MobileDeviceRepository;
import co.xarx.trix.persistence.NetworkRepository;
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

	private NotificationService notificationService;
	private AsyncService asyncService;
	private PostConverter postConverter;
	private TrixAuthenticationProvider authProvider;
	private NetworkRepository networkRepository;
	private MobileDeviceRepository mobileDeviceRepository;

	@Autowired
	public MobileService(NotificationService notificationService, AsyncService asyncService,
						 PostConverter postConverter, TrixAuthenticationProvider authProvider,
						 NetworkRepository networkRepository, MobileDeviceRepository mobileDeviceRepository) {
		this.notificationService = notificationService;
		this.asyncService = asyncService;
		this.postConverter = postConverter;
		this.authProvider = authProvider;
		this.networkRepository = networkRepository;
		this.mobileDeviceRepository = mobileDeviceRepository;
	}

	public List<Notification> sendNotifications(Post post, List<MobileDevice> mobileDevices) throws NotificationException {
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
			if (futureAndroidNotifications != null)
				futureAndroidNotifications.cancel(true);
			throw new NotificationException(e);
		}

		List<Notification> notifications;

		try {
			notifications = futureAndroidNotifications.get();
		} catch (InterruptedException | ExecutionException e) {
			notifications = notificationService.getNotifications(androidDevices,
					notification, post, "Execution error",
					Notification.Status.SEND_ERROR, Notification.DeviceType.ANDROID);
		}

		try {
			notifications.addAll(futureAppleNotifications.get());
		} catch (InterruptedException | ExecutionException e) {
			notifications.addAll(notificationService.getNotifications(appleDevices,
					notification, post, "Execution error",
					Notification.Status.SEND_ERROR, Notification.DeviceType.APPLE));
		}

		return notifications;
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

	public void updateDevice(String deviceCode, Double lat, Double lng, MobileDevice.Type type) {
		Assert.hasText(deviceCode, "Device code must not be empty");

		MobileDevice device = mobileDeviceRepository.findOne(QMobileDevice.mobileDevice.deviceCode.eq(deviceCode));
		Person person = authProvider.getLoggedPerson();

		if (person.id == 0) {
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

		mobileDeviceRepository.save(device);
	}
}
