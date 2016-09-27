package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.api.PostView;
import co.xarx.trix.api.v2.PersonalNotificationData;
import co.xarx.trix.api.v2.request.MessageFCM;
import co.xarx.trix.services.notification.NotificationService;
import co.xarx.trix.services.notification.PersonalNotificationService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2NotificationsApi;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.List;

@Component
public class V2NotificationsResource extends AbstractResource implements V2NotificationsApi {

	@Autowired
	private DateTimeFormatter dateTimeFormatter;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private PersonalNotificationService personalNotificationService;

	@Override
	public Response getUserNotifications(Integer personId) {
		List<PersonalNotificationData> notifications = personalNotificationService.getNotifications(personId);

		return Response.ok().entity(notifications).build();
	}

	@Override
	public Response sendPostNotification(String title, String message, Integer postId) {
		notificationService.createPostNotification(title, message, postId);
		return Response.ok().build();
	}

	@Override
	public Response schedulePostNotification(String title, String date, String message, Integer postId) {
		DateTime dateTime = dateTimeFormatter.parseDateTime(date);

		try {
			notificationService.schedulePostNotification(dateTime.toDate(), title, message, postId);
		} catch (SchedulerException e) {
			e.printStackTrace();
			return Response.serverError().entity("Error scheduling post").build();
		}

		return Response.ok().build();
	}

	@Override
	public Response setNotificationSeen(MessageFCM message) {
		String messageId = message.id;
		notificationService.setNotificationSeen(messageId);
		return Response.ok().build();
	}

//	@Override
//	public Response notifyLater(Map<String, String> params) {
//		CreateNotificationForm form;
//		try {
//			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
//			Date scheduledDate = simpleDateFormat.parse(params.get("scheduledDate"));
//			form = CreateNotificationForm.scheduled(scheduledDate, params);
//		} catch (ParseException e) {
//			return Response.status(Response.Status.BAD_REQUEST).entity("Date is with wrong format").build();
//		}
//		NotificationData data = notificationService.createScheduled(form.getTitle(), form.getMessage(),
//				form.getScheduledDate(), form.getProperties());

//		return Response.ok().build();
//	}
}
