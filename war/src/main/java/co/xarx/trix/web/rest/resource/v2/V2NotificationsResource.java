package co.xarx.trix.web.rest.resource.v2;

import co.xarx.trix.services.notification.NotificationService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v2.V2NotificationsApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.Response;
import java.util.Map;

@Component
public class V2NotificationsResource extends AbstractResource implements V2NotificationsApi {

	@Autowired
	private NotificationService notificationService;

	@Override
	public Response getUserNotifications() {
		return null;
	}

	@Override
	public Response sendPostNotification(String title, String message, Integer postId) {
		notificationService.createPostNotification(title, message, postId);
		return Response.ok().build();
	}

	@Override
	public Response notifyLater(Map<String, String> params) {
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

		return Response.ok().build();
	}
}
