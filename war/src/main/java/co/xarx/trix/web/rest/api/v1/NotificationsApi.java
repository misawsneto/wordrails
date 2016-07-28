package co.xarx.trix.web.rest.api.v1;

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
	@PreAuthorize("isAuthenticated()")
	ContentResponse<List<NotificationView>> searchNotifications(@QueryParam("query") String query,
																@QueryParam("page") @DefaultValue("0") Integer page,
																@QueryParam("size") @DefaultValue("20") Integer size);
}
