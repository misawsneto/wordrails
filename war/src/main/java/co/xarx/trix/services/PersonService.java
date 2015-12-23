package co.xarx.trix.services;

import co.xarx.trix.domain.Person;
import co.xarx.trix.exception.UnauthorizedException;
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

	public boolean toggleBookmark(Person person, Integer postId) {
		if(person == null || person.username.equals("wordrails"))
			throw new UnauthorizedException();

		boolean bookmarkInserted = false;
		if(!person.getBookmarkPosts().contains(postId)) {
			bookmarkInserted = true;
			person.getBookmarkPosts().add(postId);
		} else {
			person.getBookmarkPosts().remove(postId);
		}

		personRepository.save(person);
		return bookmarkInserted;
	}
}
