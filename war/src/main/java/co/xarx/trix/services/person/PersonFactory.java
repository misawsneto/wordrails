package co.xarx.trix.services.person;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.social.SocialUser;

public interface PersonFactory {
	Person create(String name, String email, String password, boolean validated) throws PersonAlreadyExistsException;

	Person create(String name, String username, String email, String password, boolean validated) throws PersonAlreadyExistsException;

	Person create(String name, String email, SocialUser socialUser) throws PersonAlreadyExistsException;
}
