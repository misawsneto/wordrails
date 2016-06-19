package co.xarx.trix.services.person;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.persistence.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonFactoryImpl implements PersonFactory {

	private PersonRepository personRepository;

	@Autowired
	public PersonFactoryImpl(PersonRepository personRepository) {
		this.personRepository = personRepository;
	}

	@Override
	public Person create(String name, String email, User user) throws PersonAlreadyExistsException {
		if (personRepository.findByUsername(user.getUsername()) != null) {
			throw new PersonAlreadyExistsException("User with username " + user.getUsername() + " already exists");
		} else if (personRepository.findByEmail(email) != null) {
			throw new PersonAlreadyExistsException("User with email " + email + " already exists");
		}

		Person person = newPerson(name, user.getUsername(), email);
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
}
