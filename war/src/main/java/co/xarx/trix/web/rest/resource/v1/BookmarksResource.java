package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.converter.PostConverter;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.PostRepository;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.BookmarksApi;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@NoArgsConstructor
public class BookmarksResource extends AbstractResource implements BookmarksApi {

	private PostSearchService postSearchService;
	private PersonRepository personRepository;
	private PersonService personService;
	private AuthService authProvider;
	private PostRepository postRepository;
	private PostConverter postConverter;
	private QueryPersistence queryPersistence;

	@Autowired
	public BookmarksResource(PostSearchService postSearchService, PersonRepository personRepository,
							 PersonService personService, AuthService authProvider, PostRepository postRepository,
							 PostConverter postConverter, QueryPersistence queryPersistence) {
		this.postSearchService = postSearchService;
		this.personRepository = personRepository;
		this.personService = personService;
		this.authProvider = authProvider;
		this.postRepository = postRepository;
		this.postConverter = postConverter;
		this. queryPersistence = queryPersistence;
	}

	@Override
	public ContentResponse<List<PostView>> 	searchBookmarks(String q, Integer page, Integer size) {
		Person person = authProvider.getLoggedPerson();

		Pair<Integer, List<PostView>> postsViews = postSearchService.searchPosts(q, person.getId(),
				page, size, person.getBookmarkPosts());

		List<Integer> ids = new ArrayList<Integer>();

		for (PostView pv: postsViews.getRight()){
			ids.add(pv.postId);
		}

		List<Post> posts = postRepository.findAll(ids);

		List<PostView> pvs = new ArrayList<>();

		for(Post post: posts){
			pvs.add(postConverter.convertTo(post));
		}

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = pvs;

		return response;
	}

	@Override
	public BooleanResponse toggleBookmark(Integer postId) {
		Person person = authProvider.getLoggedPerson();
		Person originalPerson = personRepository.findOne(person.id);

		BooleanResponse br = new BooleanResponse();

		if(originalPerson.bookmarkPosts.contains(postId)){
			originalPerson.bookmarkPosts.remove(postId);
			person.bookmarkPosts.remove(postId);
			br.response = false;
		}else{
			originalPerson.bookmarkPosts.add(postId);
			person.bookmarkPosts.add(postId);
			br.response = true;
		}

		personRepository.save(originalPerson);
		queryPersistence.updateBookmarksCount(postId);

		return br;
	}
}