package co.xarx.trix.web.rest.api.v2;

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
	@Path("/now/post/{postId}")
	Response sendPostNotification(@FormParam("title") String title,
								  @FormParam("message") String message,
								  @PathParam("postId") Integer postId);
}
