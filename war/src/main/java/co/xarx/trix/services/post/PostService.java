package co.xarx.trix.services.post;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.*;
import co.xarx.trix.exception.NotificationException;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.MobileService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostService {

	@Value("${spring.profiles.active:'dev'}")
	private String profile;

	private static final Logger log = LoggerFactory.getLogger(PostService.class);

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostConverter postConverter;
	@Autowired
	private AuthService authProvider;
	@Autowired
	private StationRepository stationRepository;
	@Autowired
	private MobileDeviceRepository mobileDeviceRepository;
	@Autowired
	private NetworkRepository networkRepository;
	@Autowired
	private MobileService mobileService;
	@Autowired
	private StationRolesRepository stationRolesRepository;
	@Autowired
	private NotificationRepository notificationRepository;

	public void sendNewPostNotification(Post post) throws NotificationException {
		List<MobileDevice> mobileDevices;
		List<StationRole> stationRoles;
		if (stationRepository.isUnrestricted(post.getStationId())) {
			mobileDevices = mobileDeviceRepository.findAll();
		} else {
			stationRoles = stationRolesRepository.findByStation(post.station);
			List<Integer> personIds = stationRoles.stream().map(stationRole -> stationRole.person.id).collect(Collectors.toList());

			personIds.remove(authProvider.getLoggedPerson().getId());

			mobileDevices = mobileDeviceRepository.findByPersonIds(personIds);
		}

		String tenantId = TenantContextHolder.getCurrentTenantId();
		Network network = networkRepository.findByTenantId(tenantId); //this should be removed in next android releases

		Collection appleDevices = mobileService.getDeviceCodes(mobileDevices, MobileDevice.Type.APPLE);
		Collection androidDevices = mobileService.getDeviceCodes(mobileDevices, MobileDevice.Type.ANDROID);

		NotificationView notification = getCreatePostNotification(post, network.id);
		List<Notification> notifications = mobileService.sendNotifications(post, notification, androidDevices, appleDevices);
		for (Notification n : notifications) {
			notificationRepository.save(n);
		}
	}

	public NotificationView getCreatePostNotification(Post post, Integer networkId) {
		String hash = StringUtil.generateRandomString(10, "Aa#");
		NotificationView notification = new NotificationView(post.title, post.title, hash, !profile.equals("prod"));
		notification.type = Notification.Type.POST_ADDED.toString();
		notification.networkId = networkId;
		notification.post = postConverter.convertTo(post);
		notification.postId = post.id;
		notification.postTitle = post.title;
		notification.postSnippet = StringUtil.simpleSnippet(post.body);
		return notification;
	}

	public void publishScheduledPost(Integer postId, boolean allowNotifications) throws NotificationException {
		Post post = postRepository.findOne(postId);
		turnPublished(allowNotifications, post);
		if (post != null) {
			post.date = new Date();
			postRepository.save(post);
		}
	}

	public void turnPublished(boolean allowNotifications, Post post) {
		if (post != null && !post.state.equals(Post.STATE_PUBLISHED)) {
			if (post.notify && allowNotifications) {
				sendNewPostNotification(post);
			}

			post.state = Post.STATE_PUBLISHED;
		}
	}
}
