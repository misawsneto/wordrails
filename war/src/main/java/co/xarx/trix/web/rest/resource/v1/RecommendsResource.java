package co.xarx.trix.web.rest.resource.v1;

import co.xarx.trix.api.BooleanResponse;
import co.xarx.trix.api.ContentResponse;
import co.xarx.trix.api.PostView;
import co.xarx.trix.domain.Person;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.QueryPersistence;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.post.PostSearchService;
import co.xarx.trix.services.security.AuthService;
import co.xarx.trix.web.rest.AbstractResource;
import co.xarx.trix.web.rest.api.v1.RecommendsApi;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Component
@NoArgsConstructor
public class RecommendsResource extends AbstractResource implements RecommendsApi {

	private PostSearchService postSearchService;
	private PersonService personService;
	private PersonRepository personRepository;
	private AuthService authProvider;
	private QueryPersistence queryPersistence;

	@Autowired
	public RecommendsResource(PostSearchService postSearchService, PersonService personService,
							  PersonRepository personRepository, AuthService authProvider, QueryPersistence queryPersistence) {
		this.postSearchService = postSearchService;
		this.personService = personService;
		this.personRepository = personRepository;
		this.authProvider = authProvider;
		this.queryPersistence = queryPersistence;
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
		Person person = authProvider.getLoggedPerson();
		Person originalPerson = personRepository.findOne(person.id);

		BooleanResponse br = new BooleanResponse();

		if(originalPerson.recommendPosts.contains(postId)){
			originalPerson.recommendPosts.remove(postId);
			br.response = false;
		}else{
			originalPerson.recommendPosts.add(postId);
			br.response = true;
		}

		originalPerson.recommendPosts = new ArrayList<>(new HashSet<>(originalPerson.recommendPosts));

		personRepository.save(originalPerson);
		queryPersistence.updateRecommendsCount(postId);

		return br;
	}
}