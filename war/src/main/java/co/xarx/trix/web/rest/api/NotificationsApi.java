package co.xarx.trix.web.rest.api;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.NotificationView;
import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/notifications")
@Consumes(MediaType.WILDCARD)
public interface NotificationsApi {

	@GET
	@Path("/searchNotifications")
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasRole('ROLE_USER')")
	ContentResponse<List<NotificationView>> searchNotifications(@QueryParam("query") String query, @QueryParam("page") Integer page, @QueryParam("size") Integer size);
}
