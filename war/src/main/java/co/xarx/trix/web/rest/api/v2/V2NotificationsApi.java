package co.xarx.trix.web.rest.api.v2;

import co.xarx.trix.api.PostView;
import co.xarx.trix.api.v2.request.MessageFCM;
import co.xarx.trix.web.rest.resource.v2.V2NotificationsResource;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v2/notifications")
@Produces(MediaType.APPLICATION_JSON)
public interface V2NotificationsApi {

	@GET
	@Path("/person/{personId}")
	Response getUserNotifications(@PathParam("personId") Integer personId);

	@POST
	@Path("/send/post/{postId}")
	Response sendPostNotification(@FormParam("title") String title,
								  @FormParam("message") String message,
								  @PathParam("postId") Integer postId);

	@POST
	@Path("/schedule/post/{postId}")
	Response schedulePostNotification(@FormParam("title") String title,
								  @FormParam("scheduledAt") String date,
								  @FormParam("message") String message,
								  @PathParam("postId") Integer postId);

	@PUT
	@Path("/seen")
	Response setNotificationSeen(MessageFCM message);
}
