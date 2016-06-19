package co.xarx.trix.services.person;

import co.xarx.trix.domain.Person;
import co.xarx.trix.domain.User;

public interface PersonFactory {

	Person create(String name, String email, User user) throws PersonAlreadyExistsException;
}
