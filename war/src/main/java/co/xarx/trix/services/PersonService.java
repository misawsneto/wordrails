package co.xarx.trix.services;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.QPerson;
import co.xarx.trix.persistence.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.model.AlreadyExistsException;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

	private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

	private UserService userService;
	private PersonRepository personRepository;

	@Autowired
	public PersonService(UserService userService, PersonRepository personRepository) {
		this.userService = userService;
		this.personRepository = personRepository;
	}

	public boolean toggleRecommend(Person person, Integer postId) {
		boolean recommendInserted = false;
		if (person.getRecommendPosts().contains(postId)) {
			person.getRecommendPosts().remove(postId);
		} else {
			recommendInserted = true;
			person.getRecommendPosts().add(postId);
		}

		personRepository.save(person);
		return recommendInserted;
	}

	public boolean toggleBookmark(Person person, Integer postId) {
		boolean bookmarkInserted = false;
		if (person.getBookmarkPosts().contains(postId)) {
			person.getBookmarkPosts().remove(postId);
		} else {
			bookmarkInserted = true;
			person.getBookmarkPosts().add(postId);
		}

		personRepository.save(person);
		return bookmarkInserted;
	}

	public Person create(String name, String username, String password, String email) {
		if (personRepository.findOne(QPerson.person.username.eq(username)) != null) {
			throw new AlreadyExistsException("User with username " + username + " already exists");
		} else if (personRepository.findOne(QPerson.person.email.eq(email)) != null) {
			throw new AlreadyExistsException("User with email " + email + " already exists");
		}

		Person person = new Person();
		person.name = name;
		person.username = username;
		person.password = password;
		person.email = email;

		person.user = userService.create(username, password);

		personRepository.save(person);

		return person;
	}
}
