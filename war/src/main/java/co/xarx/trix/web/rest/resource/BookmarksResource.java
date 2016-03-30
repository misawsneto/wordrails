package co.xarx.trix.web.rest.resource;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Person;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.auth.AuthService;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.BookmarksApi;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookmarksResource extends AbstractResource implements BookmarksApi {

	private PostSearchService postSearchService;
	private PersonService personService;
	private AuthService authProvider;

	@Autowired
	public BookmarksResource(PostSearchService postSearchService, PersonService personService, AuthService authProvider) {
		this.postSearchService = postSearchService;
		this.personService = personService;
		this.authProvider = authProvider;
	}

	@Override
	public ContentResponse<List<PostView>> searchBookmarks(String q, Integer page, Integer size) {
		Person person = authProvider.getLoggedPerson();

		Pair<Integer, List<PostView>> postsViews = postSearchService.searchPosts(q, person.getId(),
				page, size, person.getBookmarkPosts());

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postsViews.getRight();

		return response;
	}

	@Override
	public BooleanResponse toggleBookmark(Integer postId) {
		BooleanResponse bookmarkInserted = new BooleanResponse();

		Person person = authProvider.getLoggedPerson();

		bookmarkInserted.response = personService.toggleBookmark(person, postId);

		return bookmarkInserted;
	}
}