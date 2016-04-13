package co.xarx.trix.web.rest.resource;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.NotificationView;
import co.xarx.trix.converter.NotificationConverter;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.NotificationRepository;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.NotificationsApi;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class NotificationsResource extends AbstractResource implements NotificationsApi {

	private AuthService authProvider;
	private NotificationRepository notificationRepository;
	private NotificationConverter notificationConverter;

	@Autowired
	public NotificationsResource(AuthService authProvider,
								 NotificationRepository notificationRepository,
								 NotificationConverter notificationConverter) {
		this.authProvider = authProvider;
		this.notificationRepository = notificationRepository;
		this.notificationConverter = notificationConverter;
	}

	@Override
	public ContentResponse<List<NotificationView>> searchNotifications(String query, Integer page, Integer size) {
		Person person = authProvider.getLoggedPerson();
		ContentResponse<List<NotificationView>> response = new ContentResponse<>();

		List<Notification> notifications = Lists.newArrayList(notificationRepository.findNotificationsByPersonIdOrderByDate(person.id, new PageRequest(page, size)) );

		response.content = notifications.stream().map(notification ->
				notificationConverter.convertTo(notification)).collect(Collectors.toList());
		return response;
	}
//
}