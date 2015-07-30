package com.wordrails.business;

import com.wordrails.persistence.*;
import com.wordrails.services.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeDelete;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RepositoryEventHandler(Person.class)
@Component
public class PersonEventHandler {

	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private BookmarkRepository bookmarksRepository;

	@Autowired
	private NotificationRepository notificationRepository;

	@Autowired
	private PersonNetworkRegIdRepository personNetworkRegIdRepository;

	@Autowired
	private PersonNetworkTokenRepository personNetworkTokenRepository;

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
	private UserRepository userRepository;

	@Autowired
	private TermRepository termRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PostService postService;

	@Autowired
	private CacheService cacheService;


	@HandleBeforeSave
	@Transactional
	public void handleBeforeSave(Person person) {
		Person originalPerson = personRepository.findOne(person.id);
		if (originalPerson != null && !originalPerson.name.equals(person.name)) {
			termRepository.updateTermsNamesAuthorTaxonomies(person.name, originalPerson.name);
		}
	}

	@HandleBeforeDelete
	public void handleBeforeDelete(Person person) {
		networkRolesRepository.deleteByPersonId(person.id);
		imageRepository.deleteByPersonId(person.id);
		stationRolesRepository.deleteByPersonId(person.id);

		if(person.cover != null) {
			imageRepository.delete(person.cover);
		}

		bookmarksRepository.deleteByPersonId(person.id);
		recommendRepository.deleteByPersonId(person.id);
		postReadRepository.deleteByPersonId(person.id);

		List<Post> posts = postRepository.findAllFromPerson(person.id);
		for (Post post: posts){
			postService.removePostIndex(post);
		}

		queryPersistence.setNoAuthor(person.id);

		notificationRepository.deleteByPersonId(person.id);
		personNetworkRegIdRepository.deleteByPersonId(person.id);
		personNetworkTokenRepository.deleteByPersonId(person.id);
		userRepository.delete(person.user.id);
	}

	@HandleAfterSave
	@Transactional
	public void handleAfterSave(Person person) {
		cacheService.updatePerson(person.id);
		cacheService.updatePerson(person.username);
	}

}