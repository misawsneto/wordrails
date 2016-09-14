//package co.xarx.trix.web.rest.resource.v1;
//
//import co.xarx.trix.api.ContentResponse;
//import co.xarx.trix.api.NotificationView;
//import co.xarx.trix.converter.NotificationConverter;
//import co.xarx.trix.domain.MobileNotification;
//import co.xarx.trix.domain.Person;
//import co.xarx.trix.persistence.MobileNotificationRepository;
//import co.xarx.trix.services.security.AuthService;
//import co.xarx.trix.web.rest.AbstractResource;
//import co.xarx.trix.web.rest.api.v1.NotificationsApi;
//import com.google.common.collect.Lists;
//import lombok.NoArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Component
//@NoArgsConstructor
//public class NotificationsResource extends AbstractResource implements NotificationsApi {
//
//	private AuthService authProvider;
//	private MobileNotificationRepository mobileNotificationRepository;
//	private NotificationConverter notificationConverter;
//
//	@Autowired
//	public NotificationsResource(AuthService authProvider,
//								 MobileNotificationRepository mobileNotificationRepository,
//								 NotificationConverter notificationConverter) {
//		this.authProvider = authProvider;
//		this.mobileNotificationRepository = mobileNotificationRepository;
//		this.notificationConverter = notificationConverter;
//	}
//
//	@Override
//	public ContentResponse<List<NotificationView>> searchNotifications(String query, Integer page, Integer size) {
//		Person person = authProvider.getLoggedPerson();
//		ContentResponse<List<NotificationView>> response = new ContentResponse<>();
//
//		List<MobileNotification> mobileNotifications = Lists.newArrayList(mobileNotificationRepository.findNotificationsByPersonIdOrderByDate(person.id, new PageRequest(page, size)) );
//
//		response.content = mobileNotifications.stream().map(notification ->
//				notificationConverter.convertTo(notification)).collect(Collectors.toList());
//		return response;
//	}
////
//}