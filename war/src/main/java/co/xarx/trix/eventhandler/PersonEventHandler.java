package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.Post;
import co.xarx.trix.elasticsearch.domain.ESPerson;
import co.xarx.trix.elasticsearch.repository.ESPersonRepository;
import co.xarx.trix.elasticsearch.repository.ESPostRepository;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.CacheService;
import co.xarx.trix.services.ElasticSearchService;
import co.xarx.trix.services.PersonService;
import co.xarx.trix.services.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.*;
import org.springframework.stereotype.Component;

import java.util.List;

@RepositoryEventHandler(Person.class)
@Component
public class PersonEventHandler {

	@Autowired
	private ImageRepository imageRepository;
	@Autowired
	private NotificationRepository notificationRepository;
	@Autowired
	private RecommendRepository recommendRepository;
	@Autowired
	private PostReadRepository postReadRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;
	@Autowired
	private NetworkRolesRepository networkRolesRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private PostService postService;
	@Autowired
	private CacheService cacheService;
	@Autowired
	private PersonService personService;
	@Autowired
	private ElasticSearchService elasticSearchService;
	@Autowired
	private ESPostRepository esPostRepository;
	@Autowired
	private ESPersonRepository esPersonRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private MobileDeviceRepository mobileDeviceRepository;

	@HandleBeforeSave
	public void handleBeforeSave(Person person) {
		Person originalPerson = personRepository.findOne(person.id);
		person.user = userRepository.findOne(originalPerson.user.id);
		if (originalPerson != null && !originalPerson.name.equals(person.name)) {
			termRepository.updateTermsNamesAuthorTaxonomies(person.name, originalPerson.name);
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Person person) {
		networkRolesRepository.deleteByPersonId(person.id);
		stationRolesRepository.deleteRolesByPersonId(person.id);

		if(person.cover != null) {
			imageRepository.delete(person.cover);
		}

		recommendRepository.deleteByPersonId(person.id);
		postReadRepository.deleteByPersonId(person.id);

		List<Post> posts = postRepository.findAllFromPerson(person.id);
		for (Post post: posts){
			postRepository.delete(post);
		}

		queryPersistence.setNoAuthor(person.id);

		notificationRepository.deleteByPersonId(person.id);
		mobileDeviceRepository.deleteByPersonId(person.id);
		userRepository.delete(person.user.id);
		elasticSearchService.deleteIndex(person.getId(), esPersonRepository);
	}

	@HandleAfterSave
	public void handleAfterSave(Person person) {
		elasticSearchService.saveIndex(person, ESPerson.class, esPersonRepository);
	}

	@HandleAfterCreate
	public void handleAfterCreate(Person person) {
		elasticSearchService.saveIndex(person, ESPerson.class, esPersonRepository);
	}

}