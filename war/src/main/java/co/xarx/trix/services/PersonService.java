package co.xarx.trix.services;

import co.xarx.trix.domain.Person;
import co.xarx.trix.eventhandler.PersonEventHandler;
import co.xarx.trix.persistence.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

	private final Logger log = LoggerFactory.getLogger(getClass().getSimpleName());

	@Autowired
	private PersonRepository personRepository;
	@Autowired
	private PersonEventHandler personEventHandler;

	public boolean toggleBookmark(Person person, Integer postId) {
		boolean bookmarkInserted = false;
		if(!person.getBookmarkPosts().contains(postId)) {
			bookmarkInserted = true;
			person.getBookmarkPosts().add(postId);
		} else {
			person.getBookmarkPosts().remove(postId);
		}

		personRepository.save(person);
//		personEventHandler.handleAfterSave(person);
		return bookmarkInserted;
	}
}
