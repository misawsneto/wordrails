package co.xarx.trix.services.person;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.domain.social.SocialUser;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.persistence.UserRepository;
import co.xarx.trix.services.user.UserAlreadyExistsException;
import co.xarx.trix.services.user.UserFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.Normalizer;

@Service
public class PersonFactoryImpl implements PersonFactory {

	private UserFactory userFactory;
	private UserRepository userRepository;
	private PersonRepository personRepository;

	@Autowired
	public PersonFactoryImpl(UserFactory userFactory, UserRepository userRepository, PersonRepository personRepository) {
		this.userFactory = userFactory;
		this.userRepository = userRepository;
		this.personRepository = personRepository;
	}

	@Override
	public Person create(String name, String email, String password) throws PersonAlreadyExistsException {
		Assert.notNull(email);
		Assert.notNull(password);
		String username = generateUsernameFromName(name);
		return createAndSavePerson(name, username, email, password);
	}

	@Override
	public Person create(String name, String username, String email, String password) throws PersonAlreadyExistsException {
		Assert.hasText(email);
		Assert.hasText(username);
		Assert.hasText(password);
		return createAndSavePerson(name, username, email, password);
	}

	@Override
	public Person create(String name, String email, SocialUser socialUser) throws PersonAlreadyExistsException {
		Assert.notNull(email);
		Assert.notNull(name);
		Assert.notNull(socialUser);
		String username = generateUsernameFromName(name);
		return createAndSavePerson(name, username, email, socialUser);
	}

	private Person createAndSavePerson(String name, String username, String email, Object extraParam) throws PersonAlreadyExistsException {
		if (personRepository.findByUsername(username) != null) {
			throw new PersonAlreadyExistsException("User with username " + username + " already exists");
		} else if (personRepository.findByEmail(email) != null) {
			throw new PersonAlreadyExistsException("User with email " + email + " already exists");
		}

		Person person = newPerson(name, username, email);
		User user;
		try {
			if (extraParam instanceof String) {
				user = userFactory.create(person.getUsername(), (String) extraParam);
			} else {
				user = userFactory.create(person.getUsername(), (SocialUser) extraParam);
			}
		} catch (UserAlreadyExistsException e) {
			user = userRepository.findUserByUsername(person.getUsername());
		}
		person.setUser(user);
		personRepository.save(person);
		return person;
	}

	private Person newPerson(String name, String username, String email) {
		Person person = personRepository.findByEmail(email);

		if (person == null) {
			person = new Person();
			person.name = name;
			person.username = username;
			person.email = email;
		}

		return person;
	}

	private String generateUsernameFromName(String name) {
		int i = 1;
		String originalUsername = name.toLowerCase().replace(" ", "");
		String username = Normalizer.normalize(originalUsername, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		while (userRepository.existsByUsername(username)) {
			username = originalUsername + i++;
		}
		return username;
	}
}
