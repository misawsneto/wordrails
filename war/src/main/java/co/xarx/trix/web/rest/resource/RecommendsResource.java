package co.xarx.trix.web.rest.resource;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.RecommendsApi;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@NoArgsConstructor
public class RecommendsResource extends AbstractResource implements RecommendsApi {

	private PostSearchService postSearchService;
	private PersonService personService;
	private PersonRepository personRepository;
	private AuthService authProvider;

	@Autowired
	public RecommendsResource(PostSearchService postSearchService, PersonService personService,
							  PersonRepository personRepository, AuthService authProvider) {
		this.postSearchService = postSearchService;
		this.personService = personService;
		this.personRepository = personRepository;
		this.authProvider = authProvider;
	}

	@Override
	public ContentResponse<List<PostView>> searchRecommends(String q, Integer page, Integer size) {
		Person person = personRepository.findByUsername(authProvider.getUser().getUsername());

		Pair<Integer, List<PostView>> postsViews = postSearchService.
				searchPosts(q, person.getId(), page, size, person.getRecommendPosts());

		ContentResponse<List<PostView>> response = new ContentResponse<>();
		response.content = postsViews.getRight();

		return response;
	}

	@Override
	public BooleanResponse toggleRecommend(Integer postId) {
		BooleanResponse bookmarkInserted = new BooleanResponse();

		Person person = authProvider.getLoggedPerson();
		person = personRepository.findOne(person.id); //we must ensure this object comes from DB to update it

		bookmarkInserted.response = personService.toggleRecommend(person, postId);
		personRepository.save(person);

		return bookmarkInserted;
	}
}