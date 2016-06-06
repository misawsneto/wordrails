package co.xarx.trix.web.rest.api.v1;

import org.springframework.security.access.prepost.PreAuthorize;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Path("/comments")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface CommentApi {

	@GET
	@Path("/search/findPostCommentsOrderByDate")
	@PreAuthorize("hasPermission(#postId, 'co.xarx.trix.domain.Post', 'read')")
	void findPostCommentsOrderByDate(@QueryParam("postId") Integer postId) throws IOException;

	@POST
	@Path("/")
	void postComment() throws IOException;

	@PUT
	@Path("/{id}")
	void putComment(@PathParam("id") Integer commentId) throws IOException;

}
