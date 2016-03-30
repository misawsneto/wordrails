package co.xarx.trix.web.rest;

import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.api.SearchView;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.services.post.PostSearchService;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@SuppressWarnings("Duplicates")
@Path("/search")
@Consumes(MediaType.WILDCARD)
@Component
public class SearchResource {

	private PostSearchService postSearchService;
	private PersonRepository personRepository;
	private AuthService authProvider;

	@Autowired
	public SearchResource(PostSearchService postSearchService, PersonRepository personRepository, AuthService authProvider) {
		this.postSearchService = postSearchService;
		this.personRepository = personRepository;
		this.authProvider = authProvider;
	}

	@GET
	@Path("/bookmarks")
	@PreAuthorize("isAuthenticated()")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<SearchView> bookmarks(@QueryParam("query") String q,
												 @QueryParam("page") Integer page,
												 @QueryParam("size") Integer size){
		Person person = personRepository.findByUsername(authProvider.getUser().getUsername());

		Pair<Integer, List<PostView>> postsViews = postSearchService.searchPosts(q, person.getId(),
				page, size, person.getBookmarkPosts());

		return getSearchView(postsViews);
	}

	@GET
	@Path("/recommends")
	@PreAuthorize("isAuthenticated()")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<SearchView> recommends(@QueryParam("query") String q,
												  @QueryParam("page") Integer page,
												  @QueryParam("size") Integer size) {
		Person person = personRepository.findByUsername(authProvider.getUser().getUsername());

		Pair<Integer, List<PostView>> postsViews = postSearchService.searchPosts(q, person.getId(),
				page, size, person.getRecommendPosts());

		return getSearchView(postsViews);
	}

	@GET
	@Path("/posts")
	@Produces(MediaType.APPLICATION_JSON)
	public ContentResponse<SearchView> posts(@QueryParam("query") String q,
											 @QueryParam("page") Integer page,
											 @QueryParam("size") Integer size,
											 @QueryParam("personId") Integer personId,
											 @QueryParam("sortByDate") boolean sortByDate) {

		if (q == null) q = "";

		Pair<Integer, List<PostView>> postsViews = postSearchService.searchPosts(q, personId, page, size, sortByDate);

		return getSearchView(postsViews);
	}

	public ContentResponse<SearchView> getSearchView(Pair<Integer, List<PostView>> postsViews) {
		ContentResponse<SearchView> response = new ContentResponse<>();
		response.content = new SearchView();
		response.content.hits = postsViews.getLeft();
		response.content.posts = postsViews.getRight();
		return response;
	}
}