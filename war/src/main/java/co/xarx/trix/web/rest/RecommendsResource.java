package co.xarx.trix.web.rest;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.services.auth.AuthService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/recommends")
@Consumes(MediaType.WILDCARD)
@Component
public class RecommendsResource {

	@Autowired
	private PostSearchService postSearchService;
	@Autowired
	private PersonService personService;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private AuthService authProvider;

	@GET
	@Path("/searchRecommends")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<List<PostView>> searchRecommends(@QueryParam("query") String q,
															@QueryParam("page") Integer page,
															@QueryParam("size") Integer size) {
		Person person = personRepository.findByUsername(authProvider.getUser().getUsername());

		Pair<Integer, List<PostView>> postsViews = postSearchService.
				searchPosts(q, person.getId(), page, size, person.getRecommendPosts());

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postsViews.getRight();

		return response;
	}

	@PUT
	@Path("/toggleRecommend")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	@PreAuthorize("hasRole('ROLE_USER')")
	public BooleanResponse toggleRecommend(@FormParam("postId") Integer postId) {
		BooleanResponse bookmarkInserted = new BooleanResponse();

		Person person = personRepository.findByUsername(authProvider.getUser().getUsername());

		bookmarkInserted.response = personService.toggleBookmark(person, postId);

		return bookmarkInserted;
	}
}