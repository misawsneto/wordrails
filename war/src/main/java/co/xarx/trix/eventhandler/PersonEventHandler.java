package co.xarx.trix.eventhandler;

import co.xarx.trix.domain.*;
import co.xarx.trix.persistence.*;
import co.xarx.trix.services.ElasticSearchService;
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
	private PostReadRepository postReadRepository;
	@Autowired
	private StationRolesRepository stationRolesRepository;
	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private QueryPersistence queryPersistence;
	@Autowired
	private TermRepository termRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private ElasticSearchService elasticSearchService;
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
		stationRolesRepository.deleteRolesByPersonId(person.id);

		if(person.cover != null) {
			imageRepository.delete(person.cover);
		}

		postReadRepository.deleteByPersonId(person.id);

		List<Post> posts = postRepository.findAllFromPerson(person.id);
		for (Post post: posts){
			postRepository.delete(post);
		}

		queryPersistence.setNoAuthor(person.id);

		Iterable<Notification> notifications = notificationRepository.findAll(QNotification.notification.post.author.id.eq(person.id));
		notificationRepository.delete(notifications);
		mobileDeviceRepository.deleteByPersonId(person.id);
		userRepository.delete(person.user.id);
		esPersonRepository.delete(person.getId());
	}

	@HandleAfterSave
	public void handleAfterSave(Person person) {
		elasticSearchService.mapThenSave(person, ESPerson.class);
	}

	@HandleAfterCreate
	public void handleAfterCreate(Person person) {
		elasticSearchService.mapThenSave(person, ESPerson.class);
	}

}