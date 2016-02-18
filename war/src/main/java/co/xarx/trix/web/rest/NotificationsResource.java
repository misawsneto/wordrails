package co.xarx.trix.web.rest;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.NotificationView;
import co.xarx.trix.converter.NotificationConverter;
import co.xarx.trix.domain.Notification;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.NotificationRepository;
import co.xarx.trix.security.auth.TrixAuthenticationProvider;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.stream.Collectors;

@Path("/notifications")
@Consumes(MediaType.WILDCARD)
@Component
public class NotificationsResource {

	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private TrixAuthenticationProvider authProvider;

	@Autowired
	private NotificationConverter notificationConverter;

	@GET
	@Path("/searchNotifications")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<NotificationView>> searchNotifications(@QueryParam("query") String query, @QueryParam("page") Integer page, @QueryParam("size") Integer size) {
		Person person = authProvider.getLoggedPerson();
		ContentResponse<List<NotificationView>> response = new ContentResponse<>();

		List<Notification> notifications = Lists.newArrayList(
				notificationRepository.findNotificationsByPersonIdOrderByDate(person.id, new PageRequest(page, size)) );

		response.content = notifications.stream().map(notification ->
				notificationConverter.convertTo(notification)).collect(Collectors.toList());
		return response;
	}
//
}