package co.xarx.trix.web.rest;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Person;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.services.post.PostSearchService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/bookmarks")
@Consumes(MediaType.WILDCARD)
@Component
public class BookmarksResource {

	private PostSearchService postSearchService;
	private PersonService personService;
	private AuthService authProvider;

	@Autowired
	public BookmarksResource(PostSearchService postSearchService, PersonService personService, AuthService authProvider) {
		this.postSearchService = postSearchService;
		this.personService = personService;
		this.authProvider = authProvider;
	}

	@GET
	@Path("/searchBookmarks")
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("isAuthenticated()")
	public ContentResponse<List<PostView>> searchBookmarks(@QueryParam("query") String q,
															 @QueryParam("page") Integer page,
															 @QueryParam("size") Integer size){
		Person person = authProvider.getLoggedPerson();

		Pair<Integer, List<PostView>> postsViews = postSearchService.searchPosts(q, person.getId(),
				page, size, person.getBookmarkPosts());

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postsViews.getRight();

		return response;
	}

	@PUT
	@Path("/toggleBookmark")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("isAuthenticated()")
	public BooleanResponse toggleBookmark(@FormParam("postId") Integer postId){
		BooleanResponse bookmarkInserted = new BooleanResponse();

		Person person = authProvider.getLoggedPerson();

		bookmarkInserted.response = personService.toggleBookmark(person, postId);

		return bookmarkInserted;
	}
}