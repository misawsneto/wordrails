package co.xarx.trix.services.notification;

import co.xarx.trix.api.NotificationView;
import co.xarx.trix.api.PostView;
import co.xarx.trix.config.security.Permissions;
import co.xarx.trix.domain.NotificationRequest;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.MobileDeviceRepository;
import co.xarx.trix.persistence.NotificationRequestRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.services.security.PersonPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class NotificationService {

	private PostRepository postRepository;
	private PersonPermissionService personPermissionService;
	private PersonalNotificationService personalNotificationService;
	private MobileNotificationService mobileNotificationService;
	private MobileDeviceRepository mobileDeviceRepository;
	private NotificationRequestRepository notificationRequestRepository;

	@Autowired
	public NotificationService(PostRepository postRepository,
							   PersonPermissionService personPermissionService,
							   PersonalNotificationService personalNotificationService,
							   MobileNotificationService mobileNotificationService,
							   MobileDeviceRepository mobileDeviceRepository,
							   NotificationRequestRepository notificationRequestRepository) {
		this.postRepository = postRepository;
		this.personPermissionService = personPermissionService;
		this.personalNotificationService = personalNotificationService;
		this.mobileNotificationService = mobileNotificationService;
		this.mobileDeviceRepository = mobileDeviceRepository;
		this.notificationRequestRepository = notificationRequestRepository;
	}

	@Transactional
	public void createPostNotification(String title, String message, Integer postId) {
		NotificationRequest request = newNotification(title, message);
		request.setPostId(postId);

		notificationRequestRepository.save(request);

		Post post = postRepository.findOne(postId);
		List<Person> persons = personPermissionService.getPersonFromStation(post.getStationId(), Permissions.READ);

		personalNotificationService.sendNotifications(persons, request);

		List<Integer> personsIds = persons.stream().map(Person::getId).collect(Collectors.toList());
		List<String> androids = mobileDeviceRepository.findAndroids(personsIds);
		List<String> apples = mobileDeviceRepository.findApples(personsIds);

		PostView postView = new PostView(postId);
		if (post.getFeaturedImage() != null) {
			postView.setFeaturedImageHash(post.getFeaturedImage().getOriginalHash());
		}
		postView.setTitle(post.getTitle());
		postView.setDate(post.getDate());
		postView.setAuthorName(post.getAuthor().getName());
		postView.setAuthorProfilePicture(post.getAuthor().getImageHash());

		mobileNotificationService.sendNotifications(NotificationView.of(postView), androids, apples);
	}

	private NotificationRequest newNotification(String title, String message) {
		NotificationRequest notification = new NotificationRequest();
		notification.setHash(UUID.randomUUID().toString());
		notification.setTitle(title);
		notification.setMessage(message);
		return notification;
	}
}
