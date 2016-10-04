package co.xarx.trix.services.person;

import co.xarx.trix.domain.Image;
import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;
import co.xarx.trix.persistence.ImageRepository;
import co.xarx.trix.persistence.PersonRepository;
import co.xarx.trix.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PersonFactoryImpl implements PersonFactory {

	private PersonRepository personRepository;
	private ImageRepository imageRepository;

	@Autowired
	public PersonFactoryImpl(PersonRepository personRepository, ImageRepository imageRepository) {
		this.personRepository = personRepository;
		this.imageRepository = imageRepository;
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

		if(person.getImageSocialUrl() != null && !person.getImageSocialUrl().isEmpty()){
			person.image = new Image(Image.Type.PROFILE_PICTURE);
			person.image.setOriginalHash(StringUtil.toMD5(person.username));
			HashMap<String, String> hashes = new HashMap<>();
			hashes.put(Image.SIZE_LARGE, person.image.getOriginalHash());
			hashes.put(Image.SIZE_MEDIUM, person.image.getOriginalHash());
			hashes.put(Image.SIZE_SMALL, person.image.getOriginalHash());
			hashes.put(Image.SIZE_ORIGINAL, person.image.getOriginalHash());
			person.image.setHashes(hashes);
			person.image.setExternalImageUrl(person.getImageSocialUrl());
			imageRepository.save(person.image);
		}

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
