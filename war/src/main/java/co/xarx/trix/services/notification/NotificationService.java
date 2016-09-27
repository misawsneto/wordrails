package co.xarx.trix.services.notification;

import co.xarx.trix.annotation.TimeIt;
import co.xarx.trix.api.NotificationView;
import co.xarx.trix.api.PostView;
import co.xarx.trix.config.multitenancy.TenantContextHolder;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.MobileDeviceRepository;
import co.xarx.trix.persistence.MobileNotificationRepository;
import co.xarx.trix.persistence.NotificationRequestRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.SchedulerService;
import co.xarx.trix.services.notification.job.SendNotificationJob;
import co.xarx.trix.services.security.PersonPermissionService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.NotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotificationService {

	private PostRepository postRepository;
	private SchedulerService schedulerService;
	private PersonPermissionService personPermissionService;
	private PersonalNotificationService personalNotificationService;
	private MobileNotificationService mobileNotificationService;
	private MobileDeviceRepository mobileDeviceRepository;
	private MobileNotificationRepository mobileNotificationRepository;
	private NotificationRequestRepository notificationRequestRepository;

	@Autowired
	public NotificationService(PostRepository postRepository,
							   SchedulerService schedulerService,
							   PersonPermissionService personPermissionService,
							   PersonalNotificationService personalNotificationService,
							   MobileNotificationService mobileNotificationService,
							   MobileDeviceRepository mobileDeviceRepository,
							   MobileNotificationRepository mobileNotificationRepository,
							   NotificationRequestRepository notificationRequestRepository) {
		this.postRepository = postRepository;
		this.schedulerService = schedulerService;
		this.personPermissionService = personPermissionService;
		this.personalNotificationService = personalNotificationService;
		this.mobileNotificationService = mobileNotificationService;
		this.mobileDeviceRepository = mobileDeviceRepository;
		this.mobileNotificationRepository = mobileNotificationRepository;
		this.notificationRequestRepository = notificationRequestRepository;
	}

	@Transactional
	public void createPostNotification(String title, String message, Integer postId) {
		NotificationRequest request = saveRequest(title, message, postId);

		Post post = postRepository.findOne(postId);
		List<Person> persons = personPermissionService.getPersonFromStation(post.getStationId(), Permissions.READ);

		personalNotificationService.sendNotifications(persons, request);

		List<Integer> personsIds = persons.stream().map(Person::getId).collect(Collectors.toList());
		List<String> androids = mobileDeviceRepository.findAndroids(personsIds);
		List<String> apples = mobileDeviceRepository.findApples(personsIds);
		List<String> fcmAndroids = mobileDeviceRepository.findAndroidFCMs(personsIds);
		List<String> fcmApples = mobileDeviceRepository.findIOSFCMs(personsIds);

		saveMobileNotifications(postId, post, androids, apples, fcmAndroids, fcmApples);
	}

	public void schedulePostNotification(Date date, String title, String message, Integer postId) throws SchedulerException {
		Map<String, String> properties = new HashMap<>();
		properties.put("title", title);
		properties.put("message", message);
		properties.put("postId", postId + "");
		properties.put("tenantId", TenantContextHolder.getCurrentTenantId());

		schedulerService.schedule(postId + "", SendNotificationJob.class, date, properties);
	}

	@TimeIt
	public void setNotificationSeen(String messageId) throws NotFoundException{
		MobileNotification notification = mobileNotificationRepository.findByMessageId(messageId);
		if(notification == null) throw new NotFoundException("Notification not found");

		notification.setSeen(true);
		mobileNotificationRepository.save(notification);
	}

	private void saveMobileNotifications(Integer postId, Post post, List<String> androids, List<String> apples, List<String> fcmAndrois, List<String> fcmApples) {
		PostView postView = new PostView(postId);
		if (post.getFeaturedImage() != null) {
			postView.setFeaturedImageHash(post.getFeaturedImage().getOriginalHash());
		}
		postView.setTitle(post.getTitle());
		postView.setDate(post.getDate());
		postView.setAuthorName(post.getAuthor().getName());
		postView.setAuthorProfilePicture(post.getAuthor().getImageHash());
		postView.setBody(post.getBody()); // this thing must disappear in 3 months. Today is July, 26th - 2016

		List<MobileNotification> mobileNotifications = mobileNotificationService.sendNotifications(NotificationView.of(postView), androids, apples, fcmAndrois, fcmApples);

		for (MobileNotification mobileNotification : mobileNotifications) {
			mobileNotification.setPostId(postId);
			mobileNotificationRepository.save(mobileNotification);
		}
	}

	private NotificationRequest saveRequest(String title, String message, Integer postId) {
		NotificationRequest request = newNotification(title, message);
		request.setType(NotificationType.POST);
		request.setPostId(postId);

		notificationRequestRepository.save(request);
		return request;
	}

	private NotificationRequest newNotification(String title, String message) {
		NotificationRequest notification = new NotificationRequest();
		notification.setHash(UUID.randomUUID().toString());
		notification.setTitle(title);
		notification.setMessage(message);
		return notification;
	}
}
